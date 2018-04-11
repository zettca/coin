package ist.sec.coin.server.domain;

import java.util.ArrayList;

public class Ledger {
    private AccountAddress address;
    private ArrayList<Transaction> transactions;
    private int balance;

    public Ledger(AccountAddress address, int amount) {
        this.address = address;
        this.transactions = new ArrayList<Transaction>();
        this.balance = amount;
    }

    public AccountAddress getAddress() {
        return address;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public int getBalance() {
        return balance;
    }
}
