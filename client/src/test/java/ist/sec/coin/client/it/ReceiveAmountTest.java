package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class ReceiveAmountTest extends BaseServiceIT {
    private String[] accounts = new String[3];

    @Before
    public void setup() {
        client.clean();
    }

    @Before
    public void createAccounts() throws RegisterException_Exception {
        int NUM_ACCOUNTS = 3;
        for (int i = 0; i < NUM_ACCOUNTS && i < keys.length; i++) {
            accounts[i] = client.register(keys[i].getPublic().getEncoded());
        }
    }

    @Test
    public void testReceiveAmount() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            SendAmountException_Exception, ReceiveAmountException_Exception, CheckAccountException_Exception {
        int balance0 = client.checkAccount(accounts[0]).getBalance();
        int balance1 = client.checkAccount(accounts[1]).getBalance();
        int amount = 2;

        TransactionView trans = newSignedTransactionView(accounts[0], accounts[1], amount, keys[0].getPrivate());
        client.sendAmount(trans);

        AccountStatusView status0 = client.checkAccount(accounts[0]);
        AccountStatusView status1 = client.checkAccount(accounts[0]);
        Assert.assertEquals(1, status0.getTransactions().size());
        Assert.assertEquals(1, status1.getTransactions().size());
        // balance only decreases after commit
        Assert.assertEquals(balance0, status0.getBalance());
        Assert.assertEquals(balance1, status1.getBalance());

        trans = signTransaction(trans, keys[1].getPrivate());
        client.receiveAmount(trans);

        status0 = client.checkAccount(accounts[0]);
        status1 = client.checkAccount(accounts[1]);
        Assert.assertEquals(0, status0.getTransactions().size()); // cleared pending transactions
        Assert.assertEquals(0, status1.getTransactions().size()); // cleared pending transactions
        Assert.assertEquals(balance0 - amount, status0.getBalance());
        Assert.assertEquals(balance1 + amount, status1.getBalance());
    }
}
