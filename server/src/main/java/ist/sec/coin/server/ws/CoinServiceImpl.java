package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.CoinException;
import ist.sec.coin.server.security.CryptoUtils;
import ist.sec.coin.server.ws.exception.*;
import ist.sec.coin.server.ws.view.AccountStatusView;
import ist.sec.coin.server.ws.view.AuditView;
import ist.sec.coin.server.ws.view.TransactionView;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.*;

@WebService(endpointInterface = "ist.sec.coin.server.ws.CoinService")
public class CoinServiceImpl implements CoinService {
    private Map<String, CoinService> peerServices;
    private Coin coin;

    public CoinServiceImpl() {
        coin = Coin.getInstance();

        try {
            updatePeers();
        } catch (UDDINamingException e) {
            System.out.println("ERROR getting peers...");
            System.out.println(e.getMessage());
        }
    }

    public CoinServiceImpl(boolean update) {
        coin = Coin.getInstance();
    }

    // ===== Auxiliary Setup Methods

    private void updatePeers() throws UDDINamingException {
        Collection<String> peerUrls = findPeers();
        peerServices = new HashMap<>();
        for (String peerUrl : peerUrls) {
            if (!peerUrl.equals(CoinServiceApp.endpointURL)) {
                /*CoinServiceImplService service = new CoinServiceImplService();
                port = service.getCoinServiceImplPort(); */
                CoinService peerService = new CoinServiceImpl(false);
                BindingProvider bindingProvider = (BindingProvider) peerService;
                Map<String, Object> requestContext = bindingProvider.getRequestContext();
                requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, peerUrl);
                peerServices.put(peerUrl, peerService);
                peerService.noticeMeSenpai(CoinServiceApp.endpointURL);
            }
        }
    }

    private Collection<String> findPeers() throws UDDINamingException {
        UDDINaming uddiNaming = new UDDINaming(CoinServiceApp.uddiURL);
        Collection<String> coinServices = uddiNaming.list(CoinServiceApp.endpointName);
        System.out.println("Found peer services: " + coinServices);
        return coinServices;
    }

    // ===== CoinService Methods

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
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
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
            return newAccountStatusView(balance, pendingTransactions);
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
            e.printStackTrace();
            throw new ReceiveAmountException(e.getMessage());
        }
    }

    @Override
    public AuditView audit(String address) throws AuditException {
        try {
            List<Transaction> accountTransactions = coin.getAccountDoneTransactions(new AccountAddress(address));
            return newAuditView(accountTransactions);
        } catch (CoinException e) {
            throw new AuditException(e.getMessage());
        }
    }

    @Override
    public void clean() {
        System.out.println("Clearing all data...");
        coin.clean();
    }

    @Override
    public void noticeMeSenpai(String wsURL) {
        System.out.println(String.format("Service at %s joined the network...", wsURL));
        //TODO: implement some refresh thing
        if (!peerServices.containsKey(wsURL)) {
            peerServices.put(wsURL, new CoinServiceImpl());
        }
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

    private TransactionView newTransactionView(Transaction transaction) {
        TransactionView trans = new TransactionView();
        trans.setUid(transaction.getId());
        trans.setSource(transaction.getSource().getFingerprint());
        trans.setDestination(transaction.getDestination().getFingerprint());
        trans.setAmount(transaction.getAmount());
        trans.setSourceSignature(transaction.getSourceSignature());
        trans.setDestinationSignature(transaction.getDestinationSignature());
        return trans;
    }

    private AccountStatusView newAccountStatusView(int balance, List<Transaction> pendingTransactions) {
        AccountStatusView accountStatusView = new AccountStatusView();
        List<TransactionView> transactions = new ArrayList<>();

        for (Transaction transaction : pendingTransactions) {
            transactions.add(newTransactionView(transaction));
        }

        accountStatusView.setBalance(balance);
        accountStatusView.setPendingTransactions(transactions);
        return accountStatusView;
    }

    private AuditView newAuditView(List<Transaction> transactions) {
        AuditView auditView = new AuditView();
        List<TransactionView> transactionViews = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionViews.add(newTransactionView(transaction));
        }

        auditView.setTransactions(transactionViews);
        return auditView;
    }

    private List<TransactionView> newListTransactionView(List<Transaction> transactions) {
        List<TransactionView> transactionViewList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionViewList.add(newTransactionView(transaction));
        }
        return transactionViewList;
    }
}
