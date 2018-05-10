package ist.sec.coin.server.ws;

import ist.sec.coin.server.domain.AccountAddress;
import ist.sec.coin.server.domain.Coin;
import ist.sec.coin.server.domain.Transaction;
import ist.sec.coin.server.domain.exception.CoinException;
import ist.sec.coin.server.security.CryptoUtils;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.jws.WebService;
import javax.xml.ws.BindingProvider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@WebService(
        endpointInterface = "ist.sec.coin.server.ws.CoinServicePortType",
        wsdlLocation = "coin.wsdl",
        name = "Coin",
        serviceName = "CoinService",
        portName = "CoinServicePort",
        targetNamespace = "http://ws.server.coin.sec.ist/")
public class CoinServiceImpl implements CoinServicePortType {
    private Map<String, CoinServicePortType> peerServices = new HashMap<>();
    private UDDINaming uddiNaming;
    private Coin coin;

    public CoinServiceImpl() {
        coin = Coin.getInstance();

        try {
            uddiNaming = new UDDINaming(CoinServiceApp.uddiURL);
            fillPeers();
        } catch (UDDINamingException e) {
            System.out.println("Error on UDDI...");
            System.out.println(e.getMessage());
        }
    }

    // ===== Auxiliary Setup Methods

    private void fillPeers() {
        Collection<String> peerUrls = searchPeers();
        System.out.println("PEERS: " + peerUrls);
        if (peerUrls == null || peerUrls.size() == 0) return;
        peerServices.clear();
        for (String peerUrl : peerUrls) {
            if (!peerUrl.equals(CoinServiceApp.endpointURL)) {
                addPeerService(peerUrl);
                CoinServicePortType peerPort = peerServices.get(peerUrl);
                peerPort.noticeMeSenpai(CoinServiceApp.endpointURL);
            }
        }
    }

    private Collection<String> searchPeers() {
        try {
            return uddiNaming.list(CoinServiceApp.endpointName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void addPeerService(String peerEndpointURL) {
        // ignore if self or if already assigned
        if (peerEndpointURL.equals(CoinServiceApp.endpointURL) || peerServices.containsKey(peerEndpointURL)) return;

        CoinService peerService = new CoinService();
        CoinServicePortType peerPort = peerService.getCoinServicePort();
        BindingProvider bindingProvider = (BindingProvider) peerPort;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, peerEndpointURL);

        peerServices.put(peerEndpointURL, peerPort);
        System.out.println(String.format("Added %s as peer...", peerEndpointURL));
    }

    // ===== CoinService Methods

    @Override
    public String echo(String message) throws EchoException_Exception {
        if (message.trim().isEmpty()) {
            throw newEchoException("Echo message cannot be empty");
        }
        return "<" + message + ">";
    }

    @Override
    public String register(byte[] publicKeyBytes) throws RegisterException_Exception {
        try {
            PublicKey key = CryptoUtils.getPublicKeyFromString(publicKeyBytes);
            AccountAddress address = coin.registerAccount(key);
            if (!doRegisterAux(publicKeyBytes)) {
                throw newRegisterException("Error performing operation. (no consensus)");
            }
            System.out.println("Registered account: " + address.getFingerprint());
            return address.getFingerprint();
        } catch (CoinException e) {
            throw newRegisterException(e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw newRegisterException(e.getMessage());
        }
    }

    @Override
    public void sendAmount(TransactionView transactionView) throws SendAmountException_Exception {
        try {
            coin.startTransaction(newTransaction(transactionView));
            if (!doSendAmountAux(transactionView)) {
                throw newSendAmountException("Error performing operation. (no consensus)");
            }
        } catch (CoinException e) {
            throw newSendAmountException(e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            throw newSendAmountException(e.getMessage());
        }
    }

    @Override
    public AccountStatusView checkAccount(String address) throws CheckAccountException_Exception {
        try {
            AccountAddress accountAddress = new AccountAddress(address);
            int balance = coin.getAccountBalance(accountAddress);
            List<Transaction> pendingTransactions = coin.getAccountPendingTransactions(accountAddress);
            return newAccountStatusView(balance, pendingTransactions);
        } catch (CoinException e) {
            throw newCheckAccountException(e.getMessage());
        }
    }

    @Override
    public void receiveAmount(TransactionView transactionView) throws ReceiveAmountException_Exception {
        try {
            coin.commitTransaction(newTransaction(transactionView));
            if (!doReceiveAmountAux(transactionView)) {
                throw newReceiveAmountException("Error performing operation. (no consensus)");
            }
        } catch (CoinException e) {
            throw newReceiveAmountException(e.getMessage());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            throw newReceiveAmountException(e.getMessage());
        }
    }

    @Override
    public AuditView audit(String address) throws AuditException_Exception {
        try {
            List<Transaction> accountTransactions = coin.getAccountDoneTransactions(new AccountAddress(address));
            return newAuditView(accountTransactions);
        } catch (CoinException e) {
            throw newAuditException(e.getMessage());
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
        addPeerService(wsURL);
    }

    // ===== Atomic Writer Handlers

    @Override
    public boolean doRegister(byte[] publicKeyBytes) {
        try {
            PublicKey key = CryptoUtils.getPublicKeyFromString(publicKeyBytes);
            coin.registerAccount(key);
            System.out.println("I agreed on register");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean doSendAmount(TransactionView transactionView) {
        try {
            coin.startTransaction(newTransaction(transactionView));
            System.out.println("I agreed on send " + transactionView.getUid());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean doReceiveAmount(TransactionView transactionView) {
        try {
            coin.commitTransaction(newTransaction(transactionView));
            System.out.println("I agreed on send " + transactionView.getUid());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private boolean doRegisterAux(byte[] publicKeyBytes) {
        Set<String> peerUrls = peerServices.keySet();
        int successes = 0, failures = 0;

        System.out.println("Starting vote on register() to: " + peerUrls);

        for (CoinServicePortType peerPort : peerServices.values()) {
            boolean done = peerPort.doRegister(publicKeyBytes);
            System.out.println(String.format("PEER said %s!", done ? "YES" : "NO"));
            if (done) {
                successes++;
            } else {
                failures++;
            }
        }

        return (peerUrls.size() <= 1 || successes > failures);
    }

    private boolean doSendAmountAux(TransactionView transactionView) {
        Set<String> peerUrls = peerServices.keySet();
        int successes = 0, failures = 0;

        System.out.println("Starting vote on sendAmount() to: " + peerUrls);

        for (CoinServicePortType peerPort : peerServices.values()) {
            boolean done = peerPort.doSendAmount(transactionView);
            System.out.println(String.format("PEER said %s!", done ? "YES" : "NO"));
            if (done) {
                successes++;
            } else {
                failures++;
            }
        }

        return (peerUrls.size() <= 1 || successes > failures);
    }

    private boolean doReceiveAmountAux(TransactionView transactionView) {
        Set<String> peerUrls = peerServices.keySet();
        int successes = 0, failures = 0;

        System.out.println("Starting vote on receiveAmount() to: " + peerUrls);

        for (CoinServicePortType peerPort : peerServices.values()) {
            boolean done = peerPort.doReceiveAmount(transactionView);
            System.out.println(String.format("PEER said %s!", done ? "YES" : "NO"));
            if (done) {
                successes++;
            } else {
                failures++;
            }
        }

        return (peerUrls.size() <= 1 || successes > failures);
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
        List<TransactionView> transactionViews = new ArrayList<>();

        for (Transaction transaction : pendingTransactions) {
            transactionViews.add(newTransactionView(transaction));
        }

        accountStatusView.setBalance(balance);
        List<TransactionView> transactions1 = accountStatusView.getTransactions();
        transactions1.clear();
        transactions1.addAll(transactionViews);
        return accountStatusView;
    }

    private AuditView newAuditView(List<Transaction> transactions) {
        AuditView auditView = new AuditView();
        List<TransactionView> transactionViews = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionViews.add(newTransactionView(transaction));
        }

        List<TransactionView> transactions1 = auditView.getTransactions();
        transactions1.clear();
        transactions1.addAll(transactionViews);
        return auditView;
    }

    private List<TransactionView> newListTransactionView(List<Transaction> transactions) {
        List<TransactionView> transactionViewList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionViewList.add(newTransactionView(transaction));
        }
        return transactionViewList;
    }

    // ===== Exception Constructors

    private EchoException_Exception newEchoException(String message) {
        EchoException e = new EchoException();
        e.setMessage(message);
        return new EchoException_Exception(message, e);
    }

    private RegisterException_Exception newRegisterException(String message) {
        RegisterException e = new RegisterException();
        e.setMessage(message);
        return new RegisterException_Exception(message, e);
    }

    private SendAmountException_Exception newSendAmountException(String message) {
        SendAmountException e = new SendAmountException();
        e.setMessage(message);
        return new SendAmountException_Exception(message, e);
    }

    private CheckAccountException_Exception newCheckAccountException(String message) {
        CheckAccountException e = new CheckAccountException();
        e.setMessage(message);
        return new CheckAccountException_Exception(message, e);
    }

    private ReceiveAmountException_Exception newReceiveAmountException(String message) {
        ReceiveAmountException e = new ReceiveAmountException();
        e.setMessage(message);
        return new ReceiveAmountException_Exception(message, e);
    }

    private AuditException_Exception newAuditException(String message) {
        AuditException e = new AuditException();
        e.setMessage(message);
        return new AuditException_Exception(message, e);
    }

}
