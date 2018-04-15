package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class RegisterTest extends BaseServiceIT {

    @Test(expected = RegisterException_Exception.class)
    public void testNullCertificate() throws RegisterException_Exception {
        client.register(null);
    }

    @Test
    public void testRandomStringCertificate() throws RegisterException_Exception {
        client.register("abc123");
    }

    @Test
    public void testValidCertificate() throws CertificateException, RegisterException_Exception {
        InputStream inputStream = RegisterTest.class.getResourceAsStream("/certificates/dummy1.pem");
        CertificateFactory cf = CertificateFactory.getInstance(("X.509"));
        Certificate cert = cf.generateCertificate(inputStream);

        client.register(DatatypeConverter.printBase64Binary(cert.getEncoded()));
    }
}
