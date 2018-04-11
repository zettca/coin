package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseServiceIT {
    protected static CoinClient client;

    private static void populate() {

    }

    @BeforeClass
    public static void setup() {
        client = new CoinClient();
        populate();
    }

    @AfterClass
    public static void cleanup() {
    }
}
