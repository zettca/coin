package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Properties;

public class BaseServiceIT {
    static CoinClient client;
    static Properties properties;

    @BeforeClass
    public static void oneTimeSetup() {
        properties = new Properties();
        client = new CoinClient();
    }

    @AfterClass
    public static void oneTimeCleanup() {
    }

    @Before
    public void populate() {

    }

    @After
    public void cleanup() {
        client.clean();
    }

    /* ========== helpers ========== */

    static Certificate loadCertificateFromFile(String certFile) throws CertificateException {
        InputStream is = RegisterTest.class.getResourceAsStream("/accounts/" + certFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(is);
    }

    static String encodeCertificate(Certificate cert) throws CertificateEncodingException {
        return DatatypeConverter.printBase64Binary(cert.getEncoded());
    }
}
