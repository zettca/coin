package ist.sec.coin.client.it;

import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.List;

public class CheckAccountTest extends BaseServiceIT {
    private String[] accounts = new String[3];

    @Before
    public void setup() {
        client.clean();
    }

    @Before
    public void createAccounts() throws CertificateException, RegisterException_Exception {
        accounts[0] = client.register(loadEncodedCertificateFromFile("user1.cer"));
        accounts[1] = client.register(loadEncodedCertificateFromFile("user2.cer"));
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
        AccountStatusData accountData = client.checkAccount(accounts[0]);
        Assert.assertTrue(accountData.getBalance() > 0);
    }

    @Test
    public void testAccountNoTransactions() throws CheckAccountException_Exception {
        AccountStatusData accountData = client.checkAccount(accounts[0]);
        List<TransactionData> transactions = accountData.getTransaction();
        Assert.assertTrue(transactions.isEmpty());
    }

    @Test
    public void testAccountReturnsTransactions() throws CheckAccountException_Exception, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, SendAmountException_Exception {
        TransactionData transactionData = newTransactionData(accounts[0], accounts[1], 2);
        byte[] signature = CryptoUtils.sign(keys[0], client.transactionDataBytes(transactionData));
        transactionData.setSourceSignature(signature);
        client.sendAmount(transactionData);

        AccountStatusData accountData = client.checkAccount(accounts[0]);
        List<TransactionData> transactions = accountData.getTransaction();
        Assert.assertEquals(1, transactions.size());
    }

}
