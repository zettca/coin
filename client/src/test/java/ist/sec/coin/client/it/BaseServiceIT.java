package ist.sec.coin.client.it;

import ist.sec.coin.client.ws.CoinClient;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.TransactionView;
import org.junit.BeforeClass;

import java.security.*;
import java.util.Properties;
import java.util.UUID;

public class BaseServiceIT {
    static CoinClient client;
    static Properties properties;
    static KeyPair[] keys;

    @BeforeClass
    public static void oneTimeSetup() {
        final int NUM_ACCOUNTS = 3;
        properties = new Properties();
        client = new CoinClient();
        keys = new KeyPair[NUM_ACCOUNTS];

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

    static TransactionView signSource(TransactionView t, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String s = t.getUid() + t.getSource() + t.getDestination() + String.valueOf(t.getAmount());
        byte[] dataToSign = s.getBytes();
        byte[] signature = CryptoUtils.sign(key, dataToSign);
        t.setSourceSignature(signature);
        return t;
    }

    static TransactionView signDestination(TransactionView t, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String s = t.getUid() + t.getSource() + t.getDestination() + String.valueOf(t.getAmount());
        byte[] dataToSign = CryptoUtils.mergeByteArray(s.getBytes(), t.getSourceSignature());
        byte[] signature = CryptoUtils.sign(key, dataToSign);
        t.setDestinationSignature(signature);
        return t;
    }


    static TransactionView newSignedTransactionView(String source, String dest, int amount, PrivateKey key)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        TransactionView t = newTransactionView(source, dest, amount);
        return signSource(t, key);
    }
}
