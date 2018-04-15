package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;
import ist.sec.coin.server.ws.AccountStatus;

import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Coin {
    private static final int STARTING_BALANCE = 10;
    private static Coin instance;

    private Map<AccountAddress, Certificate> accountKeys;
    private Map<AccountAddress, Ledger> accountLedgers;

    private Coin() {
        this.accountKeys = new HashMap<>();
        this.accountLedgers = new HashMap<>();
    }

    public static synchronized Coin getInstance() {
        if (instance == null) {
            instance = new Coin();
        }

        return instance;
    }

    public synchronized void registerAccount(Certificate cert)
            throws InvalidPublicKeyException, NoSuchAlgorithmException {
        AccountAddress address = new AccountAddress(cert);

        if (this.accountKeys.containsKey(address) || this.accountLedgers.containsKey(address)) {
            throw new InvalidPublicKeyException("Public Key already registered");
        } else {
            this.accountKeys.put(address, cert);
            this.accountLedgers.put(address, new Ledger(address, STARTING_BALANCE));
        }
    }

    public synchronized void sendAmount(AccountAddress source, AccountAddress destination, int amount) {

    }

    public synchronized AccountStatus getAccountStatus(AccountAddress address) throws InvalidAccountAddressException {
        // TODO: implement listing pending transactions
        ArrayList<Transaction> pendingTransactions = null;

        return new AccountStatus(getLedger(address).getBalance(), pendingTransactions);
    }

    public synchronized void receiveAmount(AccountAddress address) {

    }

    public synchronized ArrayList<Transaction> getAccountTransactions(AccountAddress address)
            throws InvalidAccountAddressException {
        return this.getLedger(address).getTransactions();
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
