package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Test;

import javax.xml.ws.WebServiceException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class RegisterTest extends BaseServiceIT {

    @Test(expected = WebServiceException.class)
    public void testNullCertificate() throws RegisterException_Exception {
        client.register(null);
    }

    @Test(expected = RegisterException_Exception.class)
    public void testRandomStringCertificate() throws RegisterException_Exception {
        client.register("abc123");
    }

    @Test
    public void testValidCertificate() throws CertificateException, RegisterException_Exception {
        Certificate cert = loadCertificateFromFile("user1.cer");
        client.register(encodeCertificate(cert));
    }

    @Test(expected = RegisterException_Exception.class)
    public void testAlreadyRegisteredCertificate() throws CertificateException, RegisterException_Exception {
        Certificate cert = loadCertificateFromFile("user2.cer");
        client.register(encodeCertificate(cert));
        client.register(encodeCertificate(cert));
    }
}
