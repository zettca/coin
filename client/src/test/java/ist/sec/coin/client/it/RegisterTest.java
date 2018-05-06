package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
        client.register("abc123");
    }

    @Test
    public void testValidCertificate() throws CertificateException, RegisterException_Exception, KeyStoreException {
        Certificate cert = getCertificateFromKeyStore("user1");
        System.out.println(cert);
        client.register(encodeCertificate(cert));
    }

    @Test
    public void testValidX509Certificate() throws CertificateException, RegisterException_Exception, KeyStoreException {
        X509Certificate cert = (X509Certificate) getCertificateFromKeyStore("user2");
        client.register(encodeCertificate(cert));
    }

    @Test(expected = RegisterException_Exception.class)
    public void testAlreadyRegisteredCertificate() throws CertificateException, RegisterException_Exception,
            KeyStoreException {
        Certificate cert = getCertificateFromKeyStore("user2");
        client.register(encodeCertificate(cert));
        client.register(encodeCertificate(cert));
    }
}
