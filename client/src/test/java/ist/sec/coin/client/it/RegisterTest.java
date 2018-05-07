package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Before;
import org.junit.Test;

import java.security.PublicKey;

public class RegisterTest extends BaseServiceIT {

    @Before
    public void setup() {
        client.clean();
    }

    @Test(expected = Exception.class)
    public void testNullCertificate() throws RegisterException_Exception {
        client.register(null);
    }

    @Test(expected = RegisterException_Exception.class)
    public void testRandomStringCertificate() throws RegisterException_Exception {
        client.register("abc123".getBytes());
    }

    @Test
    public void testValidPublicKey() throws RegisterException_Exception {
        client.register(keys[0].getPublic().getEncoded());
    }

    @Test(expected = RegisterException_Exception.class)
    public void testAlreadyRegisteredPublicKey() throws RegisterException_Exception {
        PublicKey key = keys[1].getPublic();
        client.register(key.getEncoded());
        client.register(key.getEncoded());
    }
}
