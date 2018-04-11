package ist.sec.coin.client.ws;

public class CoinClientApp {

    public static void main(String[] args) {

        CoinClient client = new CoinClient();

        System.out.println("Client launched!");

        String echoResponse = client.echo("BlaBlaAlah!");
        System.out.println("Server responded with: " + echoResponse);
    }
}
