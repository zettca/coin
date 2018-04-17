package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.NonExistentAccountException;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.exception.*;

import javax.jws.WebService;
import java.security.cert.Certificate;
import java.util.ArrayList;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private Coin coin;

    public CoinServiceImpl() {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
        coin = Coin.getInstance();
    }

    @Override
    public String echo(String message) throws EchoException {
        if (message.trim().isEmpty()) {
            throw new EchoException("Echo message cannot be empty");
        }
        return "<" + message + ">";
    }

    @Override
    public String register(String certString) throws RegisterException {
        if (certString == null || certString.trim().isEmpty()) {
            throw new RegisterException("Invalid Account Address");
        }
        try {
            Certificate cert = CryptoUtils.getCertificateFromString(certString);
            AccountAddress address = coin.registerAccount(cert);
            System.out.println("Registered account: " + address.getFingerprint());
            return address.getFingerprint();
        } catch (Exception e) {
            throw new RegisterException(e.getMessage());
        }
    }

    @Override
    public void sendAmount(String uid, String source, String destination, int amount, byte[] signature)
            throws SendAmountException {
        if (uid.trim().isEmpty() || source.trim().isEmpty() || destination.trim().isEmpty()) {
            throw new SendAmountException();
        } else if (amount <= 0) {
            throw new SendAmountException();
        }

        try {
            Transaction transaction = new Transaction(
                    uid, new AccountAddress(source), new AccountAddress(destination), amount);
            transaction.setSourceSignature(signature);
            coin.startTransaction(transaction);
        } catch (Exception e) {
            throw new SendAmountException(e.getMessage());
        }
    }

    @Override
    public AccountStatus checkAccount(String address) throws CheckAccountException {
        AccountAddress accountAddress = new AccountAddress(address);
        try {
            int balance = coin.getAccountBalance(accountAddress);
            ArrayList<Transaction> pendingTransactions = coin.getAccountPendingTransactions(accountAddress);
            return new AccountStatus(balance, pendingTransactions);
        } catch (NonExistentAccountException e) {
            throw new CheckAccountException("Invalid Account Address");
        }
    }

    @Override
    public void receiveAmount(String transactionId, byte[] signature) throws ReceiveAmountException {
        try {
            Transaction transaction = coin.getPendingTransaction(transactionId);
            transaction.setDestinationSignature(signature);
            coin.commitTransaction(transaction);
        } catch (Exception e) {
            throw new ReceiveAmountException();
        }

    }

    @Override
    public ArrayList<Transaction> audit(String address) throws AuditException {
        try {
            return coin.getAccountTransactions(new AccountAddress(address));
        } catch (NonExistentAccountException e) {
            throw new AuditException("Invalid Account Address");
        }
    }

    @Override
    public void clean() {
        coin.clean();
    }
}
