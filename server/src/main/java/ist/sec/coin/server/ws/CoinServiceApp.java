package ist.sec.coin.server.ws;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Endpoint;
import java.security.Security;

public class CoinServiceApp {
    static String SERVICE_NAME = "coin";
    static int MAX_SERVERS = 10;
    static String uddiURL, endpointURL, endpointName, unbind;
    private static Endpoint endpoint;
    private static UDDINaming uddiNaming;

    private static void publishEndpoint() {
        System.out.println("Mounting endpoint...");
        endpoint = Endpoint.create(new CoinServiceImpl());
        endpoint.publish(endpointURL);
        System.out.println("Mounted endpoint to: " + endpointURL);
    }

    private static void stopEndpoint() throws UDDINamingException {
        System.out.println("Stopping endpoint...");
        endpoint.stop();
        uddiNaming.unbind(endpointName);
    }

    private static void clearUDDI() throws UDDINamingException {
        for (int i = 0; i < MAX_SERVERS; i++) {
            uddiNaming.unbind(SERVICE_NAME + i);
        }
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        if (args.length < 3) {
            System.err.println("ARGS USAGE: uddiUrl endpointURL endpointName");
        }

        uddiURL = args[0];
        endpointURL = args[1];
        endpointName = args[2];
        unbind = args[3];

        System.out.println(String.format("ARGS: %s %s %s %s", uddiURL, endpointURL, endpointName, unbind));

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

        System.out.println("Starting register service...");
        uddiNaming = new UDDINaming(uddiURL);
        if (unbind != null && !unbind.equals("false")) {
            clearUDDI();
        }

        publishEndpoint();

        System.in.read();

        stopEndpoint();

        System.out.println("Bye.");
        System.exit(1);
    }
}
