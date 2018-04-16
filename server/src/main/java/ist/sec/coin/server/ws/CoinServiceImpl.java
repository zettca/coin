package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;
import ist.sec.coin.server.security.CoinSignature;
import ist.sec.coin.server.ws.exception.AuditException;
import ist.sec.coin.server.ws.exception.CheckAccountException;
import ist.sec.coin.server.ws.exception.EchoException;
import ist.sec.coin.server.ws.exception.RegisterException;

import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private Coin coin;

    public CoinServiceImpl() {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
        coin = Coin.getInstance();
    }

    public String echo(String message) throws EchoException {
        if (message.trim().isEmpty()) {
            throw new EchoException("Echo message cannot be empty");
        }
        return "<" + message + ">";
    }

    public String register(String certString) throws RegisterException {
        if (certString == null || certString.trim().isEmpty()) {
            throw new RegisterException("Invalid Account Address");
        }
        try {
            Certificate cert = CoinSignature.getCertificateFromString(certString);
            AccountAddress address = coin.registerAccount(cert);
            System.out.println("Registered account: " + address.getFingerprint());
            return address.getFingerprint();
        } catch (InvalidPublicKeyException e) {
            throw new RegisterException("Invalid Account Address");
        } catch (CertificateException | NoSuchAlgorithmException e) {
            throw new RegisterException("Unexpected validation error");
        }
    }

    public void sendAmount(String source, String destination, int amount) {
        coin.sendAmount(new AccountAddress(source), new AccountAddress(destination), amount);
    }

    public AccountStatus checkAccount(String address) throws CheckAccountException {
        AccountAddress accountAddress = new AccountAddress(address);
        try {
            int balance = coin.getAccountBalance(accountAddress);
            ArrayList<Transaction> pendingTransactions = coin.getAccountPendingTransactions(accountAddress);
            return new AccountStatus(balance, pendingTransactions);
        } catch (InvalidAccountAddressException e) {
            throw new CheckAccountException("Invalid Account Address");
        }
    }

    public void receiveAmount(String address) {
        coin.receiveAmount(new AccountAddress(address));
    }

    public ArrayList<Transaction> audit(String address) throws AuditException {
        try {
            return coin.getAccountTransactions(new AccountAddress(address));
        } catch (InvalidAccountAddressException e) {
            throw new AuditException("Invalid Account Address");
        }
    }
}
