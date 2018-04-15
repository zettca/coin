package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.InvalidAccountAddressException;
import ist.sec.coin.server.domain.exception.InvalidPublicKeyException;
import ist.sec.coin.server.ws.exception.AuditException;
import ist.sec.coin.server.ws.exception.CheckAccountException;
import ist.sec.coin.server.ws.exception.RegisterException;

import javax.jws.WebService;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private Coin coin;

    public CoinServiceImpl() {
        coin = Coin.getInstance();
    }

    public String echo(String message) {
        return "<" + message + ">";
    }

    public void register(String certString) throws RegisterException {
        if (certString == null || certString.trim().isEmpty()) {
            throw new RegisterException("Invalid Account Address");
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(
                    new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certString)));
            coin.registerAccount(cert);
        } catch (InvalidPublicKeyException e) {
            throw new RegisterException("Invalid Account Address");
        } catch (CertificateException | NoSuchAlgorithmException e) {
            throw new RegisterException("Unexpected validation error");
        }
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {
        coin.sendAmount(source, destination, amount);
    }

    public AccountStatus checkAccount(AccountAddress address) throws CheckAccountException {
        try {
            return coin.getAccountStatus(address);
        } catch (InvalidAccountAddressException e) {
            throw new CheckAccountException("Invalid Account Address");
        }
    }

    public void receiveAmount(AccountAddress address) {
        coin.receiveAmount(address);
    }

    public ArrayList<Transaction> audit(AccountAddress address) throws AuditException {
        try {
            return coin.getAccountTransactions(address);
        } catch (InvalidAccountAddressException e) {
            throw new AuditException("Invalid Account Address");
        }
    }
}
