package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;

public class CheckAccountTest extends BaseServiceIT {
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

    @Test(expected = Exception.class)
    public void testNullAccount() throws CheckAccountException_Exception {
        client.checkAccount(null);
    }

    @Test(expected = CheckAccountException_Exception.class)
    public void testInvalidAccount() throws CheckAccountException_Exception {
        client.checkAccount("foabgoabgoa");
    }

    @Test
    public void testValidAccount() throws CheckAccountException_Exception {
        client.checkAccount(accounts[0]);
    }

    @Test
    public void testAccountHasBalance() throws CheckAccountException_Exception {
        AccountStatusView accountData = client.checkAccount(accounts[0]);
        Assert.assertTrue(accountData.getBalance() > 0);
    }

    @Test
    public void testAccountNoTransactions() throws CheckAccountException_Exception {
        AccountStatusView accountData = client.checkAccount(accounts[0]);
        List<TransactionView> transactions = accountData.getTransaction();
        Assert.assertTrue(transactions.isEmpty());
    }

    @Test
    public void testAccountReturnsTransactions() throws CheckAccountException_Exception, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, SendAmountException_Exception {
        TransactionView t = newSignedTransactionData(accounts[0], accounts[1], 2, keys[0].getPrivate());
        client.sendAmount(t);

        AccountStatusView accountData = client.checkAccount(accounts[0]);
        List<TransactionView> transactions = accountData.getTransaction();

        Assert.assertEquals(1, transactions.size());
    }

    @Test
    public void testAmountDecreases() throws CheckAccountException_Exception, SendAmountException_Exception,
            NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        AccountStatusView accountStatus = client.checkAccount(accounts[0]);
        int balanceBefore = accountStatus.getBalance();
        int amount = 2;

        TransactionView trans = newSignedTransactionData(accounts[0], accounts[1], amount, keys[0].getPrivate());
        client.sendAmount(trans);

        accountStatus = client.checkAccount(accounts[0]);
        Assert.assertEquals(balanceBefore - amount, accountStatus.getBalance());
    }

}
