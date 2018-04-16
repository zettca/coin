package ist.sec.coin.client.ws;

import ist.sec.coin.server.ws.EchoException_Exception;

public class CoinClientApp {

    public static void main(String[] args) {

        CoinClient client = new CoinClient();

        System.out.println("Client launched!");

        String echoResponse = "";
        try {
            echoResponse = client.echo("BlaBlaAlah!");
        } catch (EchoException_Exception e) {
            System.out.println("Echo failed! :O");
        }
        System.out.println("Server responded with: " + echoResponse);
    }
}
