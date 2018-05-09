package ist.sec.coin.server.ws;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Endpoint;
import java.security.Security;

public class CoinServiceApp {
    static String uddiURL, endpointURL, endpointName;
    private static Endpoint endpoint;
    private static UDDINaming uddiNaming;

    private static void startEndpoint() {
        System.out.println("Mounting server endpoint...");
        endpoint = Endpoint.create(new CoinServiceImpl());
        endpoint.publish(endpointURL);
        System.out.println("Mounted server endpoint to: " + endpointURL);
    }

    private static void publishEndpoint() throws UDDINamingException {
        System.out.println("Publishing server endpoint...");
        uddiNaming = new UDDINaming(uddiURL);
        uddiNaming.bind(endpointName, endpointURL);
        System.out.println(String.format("Published server endpoint as %s to %s", endpointName, uddiURL));
    }

    private static void stopEndpoint() throws UDDINamingException {
        endpoint.stop();
        uddiNaming.unbind(endpointName);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        if (args.length < 3) {
            System.err.println("ARGS USAGE: uddiUrl endpointURL endpointName");
        }

        uddiURL = args[0];
        endpointURL = args[1];
        endpointName = args[2];

        System.out.println(String.format("ARGS: %s %s %s", uddiURL, endpointURL, endpointName));

        System.out.println();
        System.out.println("KeyStore algorithms: " + Security.getAlgorithms("KeyStore"));
        System.out.println("Signature algorithms: " + Security.getAlgorithms("Signature"));
        System.out.println("MessageDigest algorithms: " + Security.getAlgorithms("MessageDigest"));
        System.out.println();

        // Catch Ctrl+C to exit properly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nIntercepted SIGINT");
            System.out.println("Stopping endpoint and unregistering service properly...");
            try {
                stopEndpoint();
            } catch (UDDINamingException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Done.");
        }));


        startEndpoint();
        publishEndpoint();

        System.in.read();

        stopEndpoint();
    }

}
