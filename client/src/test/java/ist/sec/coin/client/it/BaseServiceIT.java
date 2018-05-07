package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.TransactionData;
import org.junit.BeforeClass;

import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Properties;
import java.util.UUID;

public class BaseServiceIT {
    static CoinClient client;
    static Properties properties;
    static KeyStore keyStore;
    static KeyPair[] keys;

    @BeforeClass
    public static void oneTimeSetup() {
        properties = new Properties();
        client = new CoinClient();
        keys = new KeyPair[3];

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CryptoUtils.KEY_GEN_ALGORITHM);
            keyGen.initialize(CryptoUtils.KEY_SIZE); // use default SecureRandom
            for (int i = 0; i < keys.length; i++) {
                keys[i] = keyGen.generateKeyPair();
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating keys!");
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
            UnrecoverableKeyException, NoSuchAlgorithmException {
        return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
    }

    static TransactionData newTransactionData(String source, String dest, int amount) {
        TransactionData t = new TransactionData();
        t.setUid(UUID.randomUUID().toString());
        t.setSource(source);
        t.setDestination(dest);
        t.setAmount(amount);
        t.setSourceSignature(null);
        t.setDestinationSignature(null);
        return t;
    }

    static TransactionData signSource(TransactionData t, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String s = t.getUid() + t.getSource() + t.getDestination() + String.valueOf(t.getAmount());
        byte[] dataToSign = s.getBytes();
        byte[] signature = CryptoUtils.sign(key, dataToSign);
        t.setSourceSignature(signature);
        return t;
    }

    static TransactionData signDestination(TransactionData t, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String s = t.getUid() + t.getSource() + t.getDestination() + String.valueOf(t.getAmount());
        byte[] dataToSign = CryptoUtils.mergeByteArray(s.getBytes(), t.getSourceSignature());
        byte[] signature = CryptoUtils.sign(key, dataToSign);
        t.setDestinationSignature(signature);
        return t;
    }


    static TransactionData newSignedTransactionData(String source, String dest, int amount, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        TransactionData t = newTransactionData(source, dest, amount);
        return signSource(t, key);
    }

    static TransactionData newTransactionData(TransactionData t) {
        return newTransactionData(t.getSource(), t.getDestination(), t.getAmount());
    }
}
