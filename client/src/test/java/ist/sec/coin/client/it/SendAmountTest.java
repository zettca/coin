package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import ist.sec.coin.server.ws.SendAmountException_Exception;
import ist.sec.coin.server.ws.TransactionView;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class SendAmountTest extends BaseServiceIT {
    private String[] accounts = new String[3];

    @Before
    public void setup() {
        client.clean();
    }

    @Before
    public void createAccounts() throws RegisterException_Exception {
        final int NUM_ACCOUNTS = 3;
        for (int i = 0; i < NUM_ACCOUNTS && i < keys.length; i++) {
            accounts[i] = client.register(keys[i].getPublic().getEncoded());
        }
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testNegativeAmount() throws SendAmountException_Exception {
        client.sendAmount(newTransactionView(accounts[0], accounts[1], -4));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testZeroAmount() throws SendAmountException_Exception {
        client.sendAmount(newTransactionView(accounts[0], accounts[1], 0));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSignature() throws SendAmountException_Exception {
        client.sendAmount(newTransactionView(accounts[0], accounts[1], 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSender() throws SendAmountException_Exception {
        client.sendAmount(newTransactionView("123", accounts[1], 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidReceiver() throws SendAmountException_Exception {
        client.sendAmount(newTransactionView(accounts[0], "123", 1));
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testSendToSelf() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionView t = newSignedTransactionView(accounts[0], accounts[0], 1, keys[0].getPrivate());
        client.sendAmount(t);
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testWrongAmountSignature() throws SendAmountException_Exception, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException {
        TransactionView t = newSignedTransactionView(accounts[0], accounts[1], 2, keys[0].getPrivate());
        t.setAmount(t.getAmount() + 1);
        client.sendAmount(t);
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidAmount() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionView trans = newSignedTransactionView(accounts[1], accounts[2], 9001, keys[1].getPrivate());
        client.sendAmount(trans);
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testWrongKeySign() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionView trans = newSignedTransactionView(accounts[0], accounts[1], 1, keys[1].getPrivate());
        client.sendAmount(trans);
    }

    @Test
    public void testValidSend() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionView trans = newSignedTransactionView(accounts[0], accounts[1], 2, keys[0].getPrivate());
        client.sendAmount(trans);
    }

    @Test(expected = Exception.class)
    public void testReplayAttack() throws SendAmountException_Exception, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        TransactionView trans = newSignedTransactionView(accounts[0], accounts[1], 2, keys[0].getPrivate());
        client.sendAmount(trans);
        client.sendAmount(trans);
    }
}
