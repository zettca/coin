package ist.sec.coin.server.ws;

import javax.xml.ws.Endpoint;
import java.net.MalformedURLException;
import java.net.URL;

public class CoinServerApp {

    public static void main(String[] args) {
        // TODO: parse arguments
        // TODO: mount/start server endpoint

        if (args.length < 1) {
            System.err.println("Missing endpoint URL");
        }

        URL endpoint;

        try {
            endpoint = new URL(args[0]);
        } catch (MalformedURLException e) {
            System.err.println("Malformed endpoint URL");
            return;
        }

        System.out.println("Server launched at: " + endpoint.toString());

        Endpoint.publish(endpoint.toString(), new CoinServiceImpl());
    }

}
