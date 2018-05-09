package ist.sec.coin.client.ws;

import ist.sec.coin.server.ws.*;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.BindingProvider;
import java.util.Map;

public class CoinClient implements CoinServicePortType {
    private CoinServicePortType port;

    public CoinClient() {
        CoinService service = new CoinService();
        port = service.getCoinServicePort();
    }

    public CoinClient(String uddiURL, String wsName) {
        this();
        try {
            UDDINaming uddiNaming = new UDDINaming(uddiURL);
            String wsURL = uddiNaming.lookup(wsName);

            BindingProvider bindingProvider = (BindingProvider) port;
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsURL);
            System.out.println("Bound requests to: " + wsURL);

        } catch (UDDINamingException e) {
            System.out.println("Error on UDDI...");
            System.out.println(e.getMessage());
        }
    }

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

    @Override
    public void clean() {
        port.clean();
    }

    @Override
    public void noticeMeSenpai(String wsURL) {
    }
}
