package ist.sec.coin.client.ws;

public class CoinClientApp {

    public static void main(String[] args) {
        String endpoint = args[0];

        CoinClient client = new CoinClient(endpoint);

        System.out.println("Client launched!");

        String echoResponse = client.echo("BlaBlaAlah!");

        System.out.println("Server responded with: " + echoResponse);
    }
}
