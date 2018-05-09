package ist.sec.coin.server.ws;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Endpoint;
import java.security.Security;

public class CoinServiceApp {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("ARGS USAGE: uddiUrl endpointURL endpointName");
        }

        String uddiURL = args[0];
        String endpointURL = args[1];
        String endpointName = args[2];

        System.out.println(String.format("ARGS: %s %s %s", uddiURL, endpointURL, endpointName));

        System.out.println("Signature algorithms: " + Security.getAlgorithms("Signature"));
        System.out.println("MessageDigest algorithms: " + Security.getAlgorithms("MessageDigest"));

        System.out.println("Starting server endpoint...");
        Endpoint.publish(endpointURL, new CoinServiceImpl());
        System.out.println("Server Endpoint running at: " + endpointURL);

        try {
            System.out.println("Publishing server endpoint...");
            UDDINaming uddiNaming = new UDDINaming(uddiURL);
            uddiNaming.rebind(endpointName, endpointURL);
        } catch (UDDINamingException e) {
            e.printStackTrace();
            System.out.println("Failed publishing to UDDI...");
            return;
        }
        System.out.println(String.format("Server Endpoint published to: %s as %s", uddiURL, endpointName));
    }

}
