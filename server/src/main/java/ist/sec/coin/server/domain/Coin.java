package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.InvalidAmountException;
import ist.sec.coin.server.domain.exception.NonExistentAccountException;
import ist.sec.coin.server.domain.exception.RegisteredAccountException;
import ist.sec.coin.server.domain.exception.TamperingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;

public class Coin {
    private static final int STARTING_BALANCE = 10;
    private static Coin instance;

    private HashMap<AccountAddress, Certificate> accountKeys;
    private HashMap<AccountAddress, Ledger> accountLedgers;
    private HashMap<String, Transaction> pendingTransactions;

    private Coin() {
        this.accountKeys = new HashMap<>();
        this.accountLedgers = new HashMap<>();
        this.pendingTransactions = new HashMap<>();
    }

    public static synchronized Coin getInstance() {
        if (instance == null) {
            instance = new Coin();
        }

        return instance;
    }

    public synchronized AccountAddress registerAccount(Certificate cert)
            throws RegisteredAccountException, NoSuchAlgorithmException {
        AccountAddress address = new AccountAddress(cert);

        if (this.accountKeys.containsKey(address) || this.accountKeys.containsValue(cert) ||
                this.accountLedgers.containsKey(address)) {
            throw new RegisteredAccountException();
        } else {
            this.accountKeys.put(address, cert);
            this.accountLedgers.put(address, new Ledger(address, STARTING_BALANCE));
            return address;
        }
    }

    public synchronized void startTransaction(Transaction transaction)
            throws NonExistentAccountException, NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            TamperingException, InvalidAmountException {
        Certificate senderCertificate = getCertificate(transaction.getSource());
        Certificate receiverCertificate = getCertificate(transaction.getDestination());

        if (!transaction.validate(senderCertificate) || this.transactionIdExists(transaction)) {
            throw new TamperingException();
        } else if (this.getLedger(transaction.getSource()).getBalance() < transaction.getAmount()) {
            throw new InvalidAmountException();
        } else {
            this.pendingTransactions.put(transaction.getId(), transaction);
        }
    }

    public synchronized void commitTransaction(AccountAddress address) {

    }

    public synchronized int getAccountBalance(AccountAddress address) throws NonExistentAccountException {
        return this.getLedger(address).getBalance();
    }

    public synchronized ArrayList<Transaction> getAccountTransactions(AccountAddress address)
            throws NonExistentAccountException {
        return this.getLedger(address).getTransactions();
    }

    public synchronized ArrayList<Transaction> getAccountPendingTransactions(AccountAddress address)
            throws NonExistentAccountException {
        this.getLedger(address); // test account if exists

        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.pendingTransactions.values()) {
            if (transaction.getDestination().equals(address)) {
                transactions.add(transaction);
            }

        }
        return transactions;
    }

    /* ========== HELPING METHODS ========== */

    public synchronized void clean() {
        accountKeys.clear();
        accountLedgers.clear();
        pendingTransactions.clear();
    }

    private Ledger getLedger(AccountAddress address) throws NonExistentAccountException {
        Ledger ledger = this.accountLedgers.get(address);

        if (ledger != null) {
            return ledger;
        } else {
            throw new NonExistentAccountException("Address does not match an existing account");
        }
    }

    private Certificate getCertificate(AccountAddress address) throws NonExistentAccountException {
        Certificate key = this.accountKeys.get(address);

        if (key != null) {
            return key;
        } else {
            throw new NonExistentAccountException("Address does not match an existing account");
        }
    }

    private boolean transactionIdExists(Transaction transaction) {
        Transaction trans = this.pendingTransactions.get(transaction.getId());
        return (trans != null);
    }
}
