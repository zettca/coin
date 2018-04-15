package ist.sec.coin.client.ws;

import ist.sec.coin.server.ws.*;

public class CoinClient implements CoinService {
    private CoinService port;

    public CoinClient() {
        CoinServiceImplService service = new CoinServiceImplService();
        port = service.getCoinServiceImplPort();
    }

    public CoinClient(String endpointURL) {
        this();
    }

    public String echo(String message) {
        return port.echo(message);
    }

    public void register(String certString) throws RegisterException_Exception {
        port.register(certString);
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount)
            throws SendAmountException_Exception {
        port.sendAmount(source, destination, amount);
    }

    public AccountStatus checkAccount(AccountAddress address) throws CheckAccountException_Exception {
        return port.checkAccount(address);
    }

    public void receiveAmount(AccountAddress address) throws ReceiveAmountException_Exception {
        port.receiveAmount(address);
    }

    public ArrayList audit(AccountAddress address) throws AuditException_Exception {
        return port.audit(address);
    }
}
