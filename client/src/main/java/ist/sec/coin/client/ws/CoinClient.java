package ist.sec.coin.client.ws;

import ist.sec.coin.server.ws.*;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.BindingProvider;
import java.util.Map;

public class CoinClient implements CoinServicePortType {
    private CoinServicePortType port;
    private UDDINaming uddiNaming;

    public CoinClient() {
        CoinService service = new CoinService();
        port = service.getCoinServicePort();
    }

    public CoinClient(String uddiURL, String wsName) {
        this();
        try {
            uddiNaming = new UDDINaming(uddiURL);
            findAndBindToService(wsName);
        } catch (UDDINamingException e) {
            System.out.println("Error registering service...");
            System.out.println(e.getMessage());
        }
    }

    private String findAndBindToService(String wsName) throws UDDINamingException {
        final int NUM_SERVERS = 10;

        //TODO: maybe select a random server instead?
        for (int i = 0; i < NUM_SERVERS; i++) {
            String wsURL = uddiNaming.lookup(wsName + i);

            try {
                BindingProvider bindingProvider = (BindingProvider) port;
                Map<String, Object> requestContext = bindingProvider.getRequestContext();
                requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsURL);

                port.echo("testing connection");
                System.out.println("Binding requests to: " + wsURL);

                return wsURL;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    // ===== Main Client Methods

    @Override
    public String echo(String message) throws EchoException_Exception {
        return port.echo(message);
    }

    @Override
    public String register(byte[] publicKeyBytes) throws RegisterException_Exception {
        return port.register(publicKeyBytes);
    }

    @Override
    public void sendAmount(TransactionView transactionView) throws SendAmountException_Exception {
        port.sendAmount(transactionView);
    }

    @Override
    public AccountStatusView checkAccount(String address) throws CheckAccountException_Exception {
        return port.checkAccount(address);
    }

    @Override
    public void receiveAmount(TransactionView transactionView) throws ReceiveAmountException_Exception {
        port.receiveAmount(transactionView);
    }

    @Override
    public AuditView audit(String address) throws AuditException_Exception {
        return port.audit(address);
    }

    // ===== Auxiliary Client Test Methods

    @Override
    public void clean() {
        port.clean();
    }

    // ===== Auxiliary Server Dist/Repl Methods

    @Override
    public void noticeMeSenpai(String wsURL) {
    }

    @Override
    public void doClean() {
    }

    @Override
    public boolean doRegister(byte[] publicKeyBytes) {
        return false;
    }

    @Override
    public boolean doSendAmount(TransactionView transaction) {
        return false;
    }

    @Override
    public boolean doReceiveAmount(TransactionView transaction) {
        return false;
    }
}
