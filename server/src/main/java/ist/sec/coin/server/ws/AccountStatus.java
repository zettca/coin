package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.Transaction;

import java.util.List;

public class AccountStatus {
    private int balance;
    private List<Transaction> pendingTransactions;

    public AccountStatus(int balance, List<Transaction> pendingTransactions) {
        this.balance = balance;
        this.pendingTransactions = pendingTransactions;
    }

    public int getBalance() {
        return balance;
    }

    public List<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }
}
