package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.Properties;

public class BaseServiceIT {
    protected static CoinClient client;
    protected static Properties properties;

    private static void populate() {

    }

    @BeforeClass
    public static void setup() {
        properties = new Properties();
        client = new CoinClient();

        populate();
    }

    @AfterClass
    public static void cleanup() {
    }


}
