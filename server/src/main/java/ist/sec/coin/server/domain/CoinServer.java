package ist.sec.coin.server.domain;

import java.util.HashMap;

public class CoinServer {
    private HashMap<String, Ledger> ledgers;

    public CoinServer() {
        this.ledgers = new HashMap<String, Ledger>();
    }

    public HashMap<String, Ledger> getLedgers() {
        return ledgers;
    }
}
