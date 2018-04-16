package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;

import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;

public class Coin {
    private static final int STARTING_BALANCE = 10;
    private static Coin instance;

    private HashMap<AccountAddress, Certificate> accountKeys;
    private HashMap<AccountAddress, Ledger> accountLedgers;
    private ArrayList<Transaction> pendingTransactions;

    private Coin() {
        this.accountKeys = new HashMap<>();
        this.accountLedgers = new HashMap<>();
        this.pendingTransactions = new ArrayList<>();
    }

    public static synchronized Coin getInstance() {
        if (instance == null) {
            instance = new Coin();
        }

        return instance;
    }

    public synchronized AccountAddress registerAccount(Certificate cert)
            throws InvalidPublicKeyException, NoSuchAlgorithmException {
        AccountAddress address = new AccountAddress(cert);

        if (this.accountKeys.containsKey(address) || this.accountKeys.containsValue(cert) ||
                this.accountLedgers.containsKey(address)) {
            throw new InvalidPublicKeyException("Public Key already registered");
        } else {
            this.accountKeys.put(address, cert);
            this.accountLedgers.put(address, new Ledger(address, STARTING_BALANCE));
            return address;
        }
    }

    public synchronized void sendAmount(AccountAddress source, AccountAddress destination, int amount) {

    }

    public synchronized void receiveAmount(AccountAddress address) {

    }

    public synchronized int getAccountBalance(AccountAddress address) throws InvalidAccountAddressException {
        return this.getLedger(address).getBalance();
    }

    public synchronized ArrayList<Transaction> getAccountTransactions(AccountAddress address)
            throws InvalidAccountAddressException {
        return this.getLedger(address).getTransactions();
    }

    public synchronized ArrayList<Transaction> getAccountPendingTransactions(AccountAddress address)
            throws InvalidAccountAddressException {
        this.getLedger(address); // test account if exists

        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.pendingTransactions) {
            if (transaction.getDestination().equals(address)) {
                transactions.add(transaction);
            }

        }
        return transactions;
    }

    /* ========== HELPING METHODS ========== */

    private Ledger getLedger(AccountAddress address) throws InvalidAccountAddressException {
        Ledger ledger = this.accountLedgers.get(address);

        if (ledger != null) {
            return ledger;
        } else {
            throw new InvalidAccountAddressException("Address does not match an existing account");
        }
    }

    private Certificate getCertificate(AccountAddress address) throws InvalidAccountAddressException {
        Certificate key = this.accountKeys.get(address);

        if (key != null) {
            return key;
        } else {
            throw new InvalidAccountAddressException("Address does not match an existing account");
        }
    }
}
