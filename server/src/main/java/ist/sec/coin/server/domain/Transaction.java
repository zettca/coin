package ist.sec.coin.server.domain;

public class Transaction {
    private String source;
    private String destination;
    private int amount;
    // TODO: implement transaction signing

    public Transaction(String source, String destination, int amount) {
        this.source = source;
        this.destination = destination;
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getAmount() {
        return amount;
    }
}
