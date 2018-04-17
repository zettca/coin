package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.RegisterException_Exception;
import ist.sec.coin.server.ws.SendAmountException_Exception;
import org.junit.Before;
import org.junit.Test;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.UUID;

public class SendAmountTest extends BaseServiceIT {
    private String[] accounts = new String[3];

    @Before
    public void createAccounts() throws CertificateException, RegisterException_Exception {
        for (int i = 0; i < 3; i++) {
            Certificate cert = loadCertificateFromFile(String.format("user%d.cer", i + 1));
            accounts[i] = client.register(encodeCertificate(cert));
        }
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testNegativeAmount() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = -4;
        client.sendAmount(uid, accounts[0], accounts[1], amount, "".getBytes());
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testZeroAmount() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = 0;
        client.sendAmount(uid, accounts[0], accounts[1], amount, "".getBytes());
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSignature() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = 1;
        client.sendAmount(uid, accounts[0], accounts[1], amount, "".getBytes());
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidSender() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = 1;
        client.sendAmount(uid, "123", accounts[1], amount, "".getBytes());
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidReceiver() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = 1;
        client.sendAmount(uid, accounts[0], "123", amount, "".getBytes());
    }

    @Test(expected = SendAmountException_Exception.class)
    public void testInvalidAmount() throws SendAmountException_Exception {
        String uid = UUID.randomUUID().toString();
        int amount = 9001;
        client.sendAmount(uid, accounts[0], accounts[1], amount, "".getBytes());
    }
}
