package ist.sec.coin.client.ws;

public class CoinClientApp {

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        if (args.length < 2) {
            System.err.println("ARGS USAGE: uddiUrl wsName");
        }

        String uddiURL = args[0];
        String wsName = args[1];

        System.out.println(String.format("ARGS: %s %s", uddiURL, wsName));

        CoinClient client = new CoinClient(uddiURL, wsName);

        String echoResponse = client.echo("BlaBlaAlah!");
        System.out.println("Server responded with: " + echoResponse);
    }
}
