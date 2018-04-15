package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Coin {
    private static final int STARTING_BALANCE = 10;
    private static Coin instance;

    private Map<AccountAddress, PKey> accountKeys;
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

    public void registerAccount(PKey publicKey) throws InvalidPublicKeyException {
        AccountAddress address = new AccountAddress(publicKey);

        if (this.accountKeys.containsKey(address) || this.accountLedgers.containsKey(address)) {
            throw new InvalidPublicKeyException("Public Key already registered");
        } else {
            this.accountKeys.put(address, publicKey);
            this.accountLedgers.put(address, new Ledger(address, STARTING_BALANCE));
        }
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {

    }

    public AccountStatus getAccountStatus(AccountAddress address) throws InvalidAccountAddressException {
        // TODO: implement listing pending transactions
        ArrayList<Transaction> pendingTransactions = null;

        return new AccountStatus(getLedger(address).getBalance(), pendingTransactions);
    }

    public void receiveAmount(AccountAddress address) {

    }

    public ArrayList<Transaction> getAccountTransactions(AccountAddress address) throws InvalidAccountAddressException {
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

    private PKey getKey(AccountAddress address) throws InvalidAccountAddressException {
        PKey key = this.accountKeys.get(address);

        if (key != null) {
            return key;
        } else {
            throw new InvalidAccountAddressException("Address does not match an existing account");
        }
    }
}
