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
import java.security.PublicKey;
import java.security.SignatureException;
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
    public String register(byte[] publicKeyBytes) throws RegisterException {
        try {
            PublicKey key = CryptoUtils.getPublicKeyFromString(publicKeyBytes);
            AccountAddress address = coin.registerAccount(key);
            System.out.println("Registered account: " + address.getFingerprint());
            return address.getFingerprint();
        } catch (Exception e) {
            throw new RegisterException(e.getMessage());
        }
    }

    @Override
    public void sendAmount(TransactionView transactionView)
            throws SendAmountException {
        try {
            Transaction transaction = newTransaction(transactionView);
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
    public AccountStatusView checkAccount(String address) throws CheckAccountException {
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
    public void receiveAmount(TransactionView transactionView) throws ReceiveAmountException {
        try {
            Transaction transaction = newTransaction(transactionView);
            coin.commitTransaction(transaction);
        } catch (CoinException e) {
            throw new ReceiveAmountException(e.getMessage());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new ReceiveAmountException(e.getMessage());
        }
    }

    @Override
    public ArrayList<TransactionView> audit(String address) throws AuditException {
        try {
            List<Transaction> accountTransactions = coin.getAccountDoneTransactions(new AccountAddress(address));
            return (ArrayList<TransactionView>) newListTransactionData(accountTransactions);
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

    private Transaction newTransaction(TransactionView transactionView) {
        Transaction trans = new Transaction(
                transactionView.getUid(),
                new AccountAddress(transactionView.getSource()),
                new AccountAddress(transactionView.getDestination()),
                transactionView.getAmount());
        trans.setSourceSignature(transactionView.getSourceSignature());
        trans.setDestinationSignature(transactionView.getDestinationSignature());
        return trans;
    }

    private TransactionView newTransactionData(Transaction transaction) {
        TransactionView trans = new TransactionView();
        trans.setUid(transaction.getId());
        trans.setSource(transaction.getSource().getFingerprint());
        trans.setDestination(transaction.getDestination().getFingerprint());
        trans.setAmount(transaction.getAmount());
        trans.setSourceSignature(transaction.getSourceSignature());
        trans.setDestinationSignature(transaction.getDestinationSignature());
        return trans;
    }

    private AccountStatusView newAccountStatusData(int balance, List<Transaction> pendingTransactions) {
        AccountStatusView accountStatusView = new AccountStatusView();
        List<TransactionView> transactions = new ArrayList<>();

        for (Transaction transaction : pendingTransactions) {
            transactions.add(newTransactionData(transaction));
        }

        accountStatusView.setBalance(balance);
        accountStatusView.setPendingTransactions(transactions);
        return accountStatusView;
    }

    private List<TransactionView> newListTransactionData(List<Transaction> transactions) {
        List<TransactionView> transactionViewList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionViewList.add(newTransactionData(transaction));
        }
        return transactionViewList;
    }
}
