package ist.sec.coin.server.ws;

import javax.xml.ws.Endpoint;

public class CoinServerApp {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Missing endpoint URL");
        }

        String endpoint = args[0];

        Endpoint.publish(endpoint, new CoinServiceImpl());
        System.out.println("Server running at: " + endpoint);
    }

}
