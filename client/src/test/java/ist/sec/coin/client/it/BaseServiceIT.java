package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import ist.sec.coin.server.ws.TransactionData;
import org.junit.BeforeClass;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

public class BaseServiceIT {
    static CoinClient client;
    static Properties properties;
    static KeyStore keyStore;
    static PrivateKey[] keys;

    @BeforeClass
    public static void oneTimeSetup() {
        properties = new Properties();
        client = new CoinClient();
        keys = new PrivateKey[3];
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(BaseServiceIT.class.getResourceAsStream("/usersKeyStore.p12"), "1nsecur3".toCharArray());
            System.out.println("Found aliases:");
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                System.out.println(alias);
            }
            keys[0] = getPrivateKeyFromKeyStore("user1", "1nsecur3");
            keys[1] = getPrivateKeyFromKeyStore("user2", "1nsecur3");
            keys[2] = getPrivateKeyFromKeyStore("user3", "1nsecur3");
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    /* ========== helpers ========== */

    static Certificate loadCertificateFromFile(String certFile) throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return cf.generateCertificate(BaseServiceIT.class.getResourceAsStream("/accounts/" + certFile));
    }

    static String encodeCertificate(Certificate cert) throws CertificateEncodingException {
        return DatatypeConverter.printBase64Binary(cert.getEncoded());
    }

    static String loadEncodedCertificateFromFile(String certFile) throws CertificateException {
        return encodeCertificate(loadCertificateFromFile(certFile));
    }

    static Certificate getCertificateFromKeyStore(String alias) throws KeyStoreException {
        return keyStore.getCertificate(alias);
    }

    static PrivateKey getPrivateKeyFromKeyStore(String alias, String password) throws KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {
        return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    }

    static TransactionData newTransactionData(String source, String dest, int amount) {
        TransactionData transaction = new TransactionData();
        transaction.setUid(UUID.randomUUID().toString());
        transaction.setSource(source);
        transaction.setDestination(dest);
        transaction.setAmount(amount);
        transaction.setSourceSignature(null);
        transaction.setDestinationSignature(null);
        return transaction;
    }
}
