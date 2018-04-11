package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.Transaction;

import java.util.ArrayList;

public class AccountStatus {
    private int balance;
    private ArrayList<Transaction> pendingTransactions;

    public AccountStatus(int balance, ArrayList<Transaction> pendingTransactions) {
        this.balance = balance;
        this.pendingTransactions = pendingTransactions;
    }

    public int getBalance() {
        return balance;
    }

    public ArrayList<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }
}
