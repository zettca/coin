package ist.sec.coin.server.domain;

import ist.sec.coin.server.domain.exception.CoinException;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class RegisterAccountTest extends DomainTest {

    @Test(expected = CoinException.class)
    public void testRegisterNullCertificate() throws CoinException, NoSuchAlgorithmException {
        coin.registerAccount(null);
    }
}
