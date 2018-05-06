package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.CoinException;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.exception.*;

import javax.jws.WebService;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

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
    public void sendAmount(TransactionData transactionData)
            throws SendAmountException {
        try {
            Transaction transaction = newTransaction(transactionData);
            coin.startTransaction(transaction);
        } catch (CoinException e) {
            throw new SendAmountException(e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new SendAmountException(e.getMessage());
        } catch (SignatureException e) {
            e.printStackTrace();
            throw new SendAmountException(e.getMessage());
        }
    }

    @Override
    public AccountStatusData checkAccount(String address) throws CheckAccountException {
        try {
            AccountAddress accountAddress = new AccountAddress(address);
            int balance = coin.getAccountBalance(accountAddress);
            List<Transaction> pendingTransactions = coin.getAccountPendingTransactions(accountAddress);
            return newAccountStatusData(balance, pendingTransactions);
        } catch (CoinException e) {
            throw new CheckAccountException(e.getMessage());
        }
    }

    @Override
    public void receiveAmount(TransactionData transactionData) throws ReceiveAmountException {
        try {
            Transaction transaction = newTransaction(transactionData);
            coin.commitTransaction(transaction);
        } catch (CoinException e) {
            throw new ReceiveAmountException(e.getMessage());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new ReceiveAmountException(e.getMessage());
        }
    }

    @Override
    public ArrayList<TransactionData> audit(String address) throws AuditException {
        try {
            List<Transaction> accountTransactions = coin.getAccountTransactions(new AccountAddress(address));
            return (ArrayList<TransactionData>) newListTransactionData(accountTransactions);
        } catch (CoinException e) {
            throw new AuditException(e.getMessage());
        }
    }

    @Override
    public void clean() {
        System.out.println("Clearing all data...");
        coin.clean();
    }

    // ===== Data Constructors

    private Transaction newTransaction(TransactionData transactionData) {
        Transaction trans = new Transaction(
                transactionData.getUid(),
                new AccountAddress(transactionData.getSource()),
                new AccountAddress(transactionData.getDestination()),
                transactionData.getAmount());
        trans.setSourceSignature(transactionData.getSourceSignature());
        trans.setDestinationSignature(transactionData.getDestinationSignature());
        return trans;
    }

    private TransactionData newTransactionData(Transaction transaction) {
        TransactionData trans = new TransactionData();
        trans.setUid(transaction.getId());
        trans.setSource(transaction.getSource().getFingerprint());
        trans.setDestination(transaction.getDestination().getFingerprint());
        trans.setAmount(transaction.getAmount());
        trans.setSourceSignature(transaction.getSourceSignature());
        trans.setDestinationSignature(transaction.getDestinationSignature());
        return trans;
    }

    private AccountStatusData newAccountStatusData(int balance, List<Transaction> pendingTransactions) {
        AccountStatusData accountStatusData = new AccountStatusData();
        List<TransactionData> transactions = new ArrayList<>();

        for (Transaction transaction : pendingTransactions) {
            transactions.add(newTransactionData(transaction));
        }

        accountStatusData.setBalance(balance);
        accountStatusData.setPendingTransactions(transactions);
        return accountStatusData;
    }

    private List<TransactionData> newListTransactionData(List<Transaction> transactions) {
        List<TransactionData> transactionDataList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionDataList.add(newTransactionData(transaction));
        }
        return transactionDataList;
    }
}
