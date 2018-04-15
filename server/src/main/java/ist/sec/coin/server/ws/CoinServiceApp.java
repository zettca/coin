package ist.sec.coin.server.ws;

import javax.xml.ws.Endpoint;
import java.security.Security;

public class CoinServiceApp {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Missing endpoint URL");
        }

        String endpoint = args[0];

        System.out.println("Signature algorithms: " + Security.getAlgorithms("Signature"));
        System.out.println("MessageDigest algorithms: " + Security.getAlgorithms("MessageDigest"));

        Endpoint.publish(endpoint, new CoinServiceImpl());
        System.out.println("Server running at: " + endpoint);
    }

}
