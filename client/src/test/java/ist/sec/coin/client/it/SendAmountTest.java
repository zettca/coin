package ist.sec.coin.client.it;

import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.RegisterException_Exception;
import ist.sec.coin.server.ws.SendAmountException_Exception;
import ist.sec.coin.server.ws.TransactionData;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;

public class SendAmountTest extends BaseServiceIT {
    private String[] accounts = new String[3];
    private PrivateKey[] keys = new PrivateKey[3];

    @Before
    public void setup() {
        client.clean();
    }

    @Before
    public void createAccounts() throws CertificateException, RegisterException_Exception {
        accounts[0] = client.register(loadEncodedCertificateFromFile("user1.cer"));
        accounts[1] = client.register(loadEncodedCertificateFromFile("user2.cer"));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testNegativeAmount() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData(accounts[0], accounts[1], -4));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testZeroAmount() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData(accounts[0], accounts[1], 0));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSignature() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData(accounts[0], accounts[1], 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSender() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData("123", accounts[1], 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidReceiver() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData(accounts[0], "123", 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidAmount() throws SendAmountException_Exception {
        client.sendAmount(newTransactionData(accounts[0], accounts[1], 9001));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testSendToSelf() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionData transactionData = newTransactionData(accounts[0], accounts[0], 1);
        byte[] sourceSignature = CryptoUtils.sign(keys[0], client.transactionDataBytes(transactionData));
        transactionData.setSourceSignature(sourceSignature);
        client.sendAmount(transactionData);
    }

    @Test
    public void testValidSend() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionData transactionData = newTransactionData(accounts[0], accounts[1], 2);
        byte[] sourceSignature = CryptoUtils.sign(keys[0], client.transactionDataBytes(transactionData));
        transactionData.setSourceSignature(sourceSignature);
        client.sendAmount(transactionData);
    }
}
