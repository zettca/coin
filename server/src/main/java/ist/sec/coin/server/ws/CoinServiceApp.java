package ist.sec.coin.server.ws;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Endpoint;
import java.security.Security;

public class CoinServiceApp {

    public static void main(String[] args) throws UDDINamingException {
        if (args.length < 1) {
            System.err.println("Missing endpoint URL");
        }

        String uddiURL = args[0];
        String endpointURL = args[1];
        String endpointName = args[2];

        System.out.println("Signature algorithms: " + Security.getAlgorithms("Signature"));
        System.out.println("MessageDigest algorithms: " + Security.getAlgorithms("MessageDigest"));

        System.out.println("Starting server endpoint...");
        Endpoint.publish(endpointURL, new CoinServiceImpl());
        System.out.println("Server Endpoint running at: " + endpointURL);

        System.out.println("Publishing server endpoint...");
        UDDINaming uddiNaming = new UDDINaming(uddiURL);
        uddiNaming.rebind(endpointName, endpointURL);
        System.out.println("Server Endpoint published to: " + uddiURL);
    }

}
