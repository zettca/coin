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

    public void register(PKey publicKey) throws InvalidPublicKeyException_Exception {
        port.register(publicKey);
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount)
            throws InvalidAmountException_Exception, InvalidAccountAddressException_Exception {
        port.sendAmount(source, destination, amount);
    }

    public AccountStatus checkAccount(AccountAddress address) throws InvalidAccountAddressException_Exception {
        return port.checkAccount(address);
    }

    public void receiveAmount(AccountAddress address) throws InvalidAccountAddressException_Exception {
        port.receiveAmount(address);
    }

    public ArrayList audit(AccountAddress address) throws InvalidAccountAddressException_Exception {
        return port.audit(address);
    }
}
