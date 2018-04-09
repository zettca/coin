package ist.sec.coin.server.domain;

import java.util.ArrayList;

public class Ledger {
    private String keyIdentifier;
    private ArrayList<Transaction> transactions;

    public Ledger(String keyIdentifier) {
        this.keyIdentifier = keyIdentifier;
        this.transactions = new ArrayList<Transaction>();
    }

    public String getIdentifier() {
        return keyIdentifier;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
