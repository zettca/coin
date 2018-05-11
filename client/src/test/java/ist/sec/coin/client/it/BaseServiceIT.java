package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.TransactionView;
import org.junit.BeforeClass;

import java.io.IOException;
import java.security.*;
import java.util.Properties;
import java.util.UUID;

public class BaseServiceIT {
    static CoinClient client;
    static Properties properties;
    static KeyPair[] keys;

    @BeforeClass
    public static void oneTimeSetup() {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        final int NUM_ACCOUNTS = 3;
        properties = new Properties();
        keys = new KeyPair[NUM_ACCOUNTS];

        try {
            properties.load(BaseServiceIT.class.getResourceAsStream("/test.properties"));
            String uddiURL = properties.getProperty("uddi.url");
            String wsName = properties.getProperty("ws.name");
            client = new CoinClient(uddiURL, wsName);

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CryptoUtils.KEY_GEN_ALGORITHM);
            keyGen.initialize(CryptoUtils.KEY_SIZE); // use default SecureRandom
            for (int i = 0; i < keys.length; i++) {
                keys[i] = keyGen.generateKeyPair();
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error generating keys!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error loading properties!");
        }
    }

    /* ========== helpers ========== */

    static TransactionView newTransactionView(String source, String dest, int amount) {
        TransactionView t = new TransactionView();
        t.setUid(UUID.randomUUID().toString());
        t.setSource(source);
        t.setDestination(dest);
        t.setAmount(amount);
        t.setSourceSignature(null);
        t.setDestinationSignature(null);
        return t;
    }

    static TransactionView signTransaction(TransactionView t, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String s = t.getUid() + t.getSource() + t.getDestination() + String.valueOf(t.getAmount());
        byte[] dataToSign = s.getBytes();
        byte[] signature = CryptoUtils.sign(key, dataToSign);
        if (t.getSourceSignature() == null) { // since signing order is sender > destination
            t.setSourceSignature(signature);
        } else {
            t.setDestinationSignature(signature);
        }
        return t;
    }

    static TransactionView newSignedTransactionView(String source, String dest, int amount, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        TransactionView t = newTransactionView(source, dest, amount);
        return signTransaction(t, key);
    }
}
