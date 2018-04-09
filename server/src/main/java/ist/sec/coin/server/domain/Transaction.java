package ist.sec.coin.server.domain;

public class Transaction {
    private String from;
    private String to;
    private int amount;

    public Transaction(String from, String to, int value) {
        this.from = from;
        this.to = to;
        this.amount = value;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getAmount() {
        return amount;
    }
}
