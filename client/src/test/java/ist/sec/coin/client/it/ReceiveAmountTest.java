package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Before;

public class ReceiveAmountTest extends BaseServiceIT {
    private String[] accounts = new String[3];

    @Before
    public void setup() {
        client.clean();
    }

    @Before
    public void createAccounts() throws RegisterException_Exception {
        int NUM_ACCOUNTS = 3;
        for (int i = 0; i < NUM_ACCOUNTS && i < keys.length; i++) {
            accounts[i] = client.register(keys[i].getPublic().getEncoded());
        }
    }
}
