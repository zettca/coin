package ist.sec.coin.server.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class DomainTest {
    private static final String KEYSTORE_PATH = "usersKeyStore.jks";
    private static final String KEYSTORE_PASS = "1nsecur3";
    private static KeyStore keyStore;

    static Coin coin;

    @BeforeClass
    public static void oneTimeSetup() {
        coin = Coin.getInstance();
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(DomainTest.class.getResourceAsStream(KEYSTORE_PATH), KEYSTORE_PASS.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void oneTimeCleanup() {
        coin.clean();
    }

    @Before
    public void populate() {

    }

    @After
    public void cleanup() {
        coin.clean();
    }

    /* ========== HELPERS ========== */

    public static PrivateKey getPrivateKeyFromKeyStore(String alias, char[] password) {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Certificate getCertificateFromKeyStore(String alias) {
        try {
            return keyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }
}
