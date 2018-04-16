package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.AccountStatus;
import ist.sec.coin.server.ws.CheckAccountException_Exception;
import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class CheckAccountTest extends BaseServiceIT {
    private String accountFingerprint;

    @Before
    public void createAccounts() throws CertificateException, RegisterException_Exception {
        Certificate cert = loadCertificateFromFile("user3.cer");
        accountFingerprint = client.register(encodeCertificate(cert));
    }

    @Test
    public void testValidAccount() throws CheckAccountException_Exception {
        AccountStatus accountStatus = client.checkAccount(accountFingerprint);
        Assert.assertTrue(accountStatus.getBalance() >= 0);
        Assert.assertTrue(accountStatus.getPendingTransactions().isEmpty());
    }

    @Test(expected = CheckAccountException_Exception.class)
    public void testInvalidAccount() throws CheckAccountException_Exception {
        client.checkAccount("foabgoabgoa");
    }
}
