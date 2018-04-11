package ist.sec.coin.client.ws;

import ist.sec.coin.server.ws.AccountAddress;
import ist.sec.coin.server.ws.CoinService;
import ist.sec.coin.server.ws.CoinServiceImplService;
import ist.sec.coin.server.ws.PKey;

public class CoinClient implements CoinService {
    private CoinService port;

    CoinClient() {
        CoinServiceImplService service = new CoinServiceImplService();
        port = service.getCoinServiceImplPort();
    }

    public String echo(String message) {
        return port.echo(message);
    }

    public void register(PKey publicKey) {
        port.register(publicKey);
    }

    public void sendAmount(AccountAddress source, AccountAddress destination, int amount) {
        port.sendAmount(source, destination, amount);
    }

    public void checkAccount(AccountAddress address) {
        port.checkAccount(address);
    }

    public void receiveAmount(AccountAddress address) {
        port.receiveAmount(address);
    }

    public void audit(AccountAddress address) {
        port.audit(address);
    }
}
