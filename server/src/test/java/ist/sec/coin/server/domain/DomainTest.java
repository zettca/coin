package ist.sec.coin.server.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class DomainTest {
    static Coin coin;

    @BeforeClass
    public static void oneTimeSetup() {
        coin = Coin.getInstance();
    }

    @AfterClass
    public static void oneTimeCleanup() {
    }

    @Before
    public void populate() {

    }

    @After
    public void cleanup() {
        coin.clean();
    }
}
