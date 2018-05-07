package ist.sec.coin.server.domain;

import java.util.ArrayList;

public class Ledger {
    private final AccountAddress address;
    private final ArrayList<Transaction> transactions;
    private int balance;

    Ledger(AccountAddress address, int amount) {
        this.address = address;
        this.transactions = new ArrayList<>();
        this.balance = amount;
    }

    public AccountAddress getAddress() {
        return address;
    }

    ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    int getBalance() {
        return balance;
    }

    void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        if (transaction.getDestination().equals(address)) {
            this.balance += transaction.getAmount();
        } else if (transaction.getSource().equals(address)) {
            this.balance -= transaction.getAmount();
        }
    }
}
