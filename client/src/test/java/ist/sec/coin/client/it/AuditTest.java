package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class AuditTest extends BaseServiceIT {
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
    public void testAudit() throws SendAmountException_Exception, ReceiveAmountException_Exception,
            AuditException_Exception, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        TransactionView t1 = newSignedTransactionView(accounts[0], accounts[1], 2, keys[0].getPrivate());
        TransactionView t2 = newSignedTransactionView(accounts[0], accounts[2], 2, keys[0].getPrivate());

        client.sendAmount(t1);
        client.sendAmount(t2);

        t1 = signTransaction(t1, keys[1].getPrivate());
        t2 = signTransaction(t2, keys[2].getPrivate());

        client.receiveAmount(t1);
        client.receiveAmount(t2);

        AuditView audit0 = client.audit(accounts[0]);
        AuditView audit1 = client.audit(accounts[1]);
        AuditView audit2 = client.audit(accounts[2]);

        Assert.assertEquals(2, audit0.getTransactions().size());
        Assert.assertEquals(1, audit1.getTransactions().size());
        Assert.assertEquals(1, audit2.getTransactions().size());
    }
}
