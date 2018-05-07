package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coin {
    private static final int STARTING_BALANCE = 10;
    private static Coin instance;

    private Map<String, PublicKey> accountKeys;
    private Map<String, Ledger> accountLedgers;
    private Map<String, Transaction> pendingTransactions;

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

    public synchronized AccountAddress registerAccount(PublicKey key)
            throws RegisteredAccountException, NoSuchAlgorithmException {
        AccountAddress address = new AccountAddress(key);

        if (this.accountKeys.containsKey(address.getFingerprint()) || this.accountLedgers.containsKey(address.getFingerprint())) {
            throw new RegisteredAccountException("Account is already registered");
        } else {
            this.accountKeys.put(address.getFingerprint(), key);
            this.accountLedgers.put(address.getFingerprint(), new Ledger(address, STARTING_BALANCE));
            return address;
        }
    }

    public synchronized void startTransaction(Transaction transaction)
            throws NonExistentAccountException, NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            TamperingException, InvalidAmountException {
        PublicKey senderKey = this.getPublicKey(transaction.getSource());
        PublicKey receiverKey = this.getPublicKey(transaction.getDestination());

        if (transaction.getAmount() <= 0) {
            throw new InvalidAmountException("Transaction amount must be positive!");
        } else if (transaction.getSource().equals(transaction.getDestination())) {
            throw new TamperingException("Sender and Destination addresses must be different");
        } else if (transaction.getSourceSignature() == null || !transaction.validateSource(senderKey)) {
            throw new TamperingException("Sender signature is incorrect");
        } else if (this.transactionIdExists(transaction)) {
            throw new TamperingException("Transaction identifier already registered");
        } else if (this.getLedger(transaction.getSource()).getBalance() < transaction.getAmount()) {
            throw new InvalidAmountException("Sender does not have enough funds");
        } else {
            // Transaction is valid!
            this.pendingTransactions.put(transaction.getId(), transaction);
        }
    }

    public synchronized void commitTransaction(Transaction transaction)
            throws NonExistentAccountException, NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            TamperingException {
        PublicKey senderKey = this.getPublicKey(transaction.getSource());
        PublicKey receiverKey = this.getPublicKey(transaction.getDestination());
        Ledger sourceLedger = this.getLedger(transaction.getSource());
        Ledger destinationLedger = this.getLedger(transaction.getDestination());

        if (!transaction.validateSource(senderKey)) {
            throw new TamperingException("Sender signature is incorrect");
        } else if (!transaction.validateDestination(receiverKey)) {
            throw new TamperingException("Receiver signature is incorrect");
        } else {
            // Transaction is valid. Commit it. TODO: commit better
            this.pendingTransactions.remove(transaction.getId());
            sourceLedger.addTransaction(transaction);
            destinationLedger.addTransaction(transaction);
        }
    }

    public synchronized int getAccountBalance(AccountAddress address) throws NonExistentAccountException {
        return this.getLedger(address).getBalance();
    }

    public synchronized List<Transaction> getAccountDoneTransactions(AccountAddress address)
            throws NonExistentAccountException {
        return this.getLedger(address).getTransactions();
    }

    public synchronized List<Transaction> getAccountPendingTransactions(AccountAddress address)
            throws NonExistentAccountException {
        this.getLedger(address); // test account if exists

        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : this.pendingTransactions.values()) {
            if (transaction.getSource().equals(address) || transaction.getDestination().equals(address)) {
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

    public synchronized Transaction getPendingTransaction(String tid) throws CoinException {
        Transaction transaction = this.pendingTransactions.get(tid);

        if (transaction != null) {
            return transaction;
        } else {
            throw new CoinException("Transaction ID does not match to a Transaction");
        }
    }

    private Ledger getLedger(AccountAddress address) throws NonExistentAccountException {
        Ledger ledger = this.accountLedgers.get(address.getFingerprint());

        if (ledger != null) {
            return ledger;
        } else {
            throw new NonExistentAccountException("Address does not match an existing account");
        }
    }

    private PublicKey getPublicKey(AccountAddress address) throws NonExistentAccountException {
        PublicKey key = this.accountKeys.get(address.getFingerprint());

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
