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

    @Override
    public String echo(String message) throws EchoException_Exception {
        return port.echo(message);
    }

    @Override
    public String register(byte[] publicKeyBytes) throws RegisterException_Exception {
        return port.register(publicKeyBytes);
    }

    @Override
    public void sendAmount(TransactionData transactionData) throws SendAmountException_Exception {
        port.sendAmount(transactionData);
    }

    @Override
    public AccountStatusData checkAccount(String address) throws CheckAccountException_Exception {
        return port.checkAccount(address);
    }

    @Override
    public void receiveAmount(TransactionData transactionData) throws ReceiveAmountException_Exception {
        port.receiveAmount(transactionData);
    }

    @Override
    public ArrayList audit(String address) throws AuditException_Exception {
        return port.audit(address);
    }

    @Override
    public void clean() {
        port.clean();
    }

}
