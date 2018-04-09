package ist.sec.coin.server.ws;

import javax.xml.ws.Endpoint;

public class CoinServerApp {

    public static void main(String[] args) {
        // TODO: parse arguments
        // TODO: mount/start server endpoint

        System.out.println("Server launched!");

        Endpoint.publish("http://localhost:8088/ws/coin", new CoinServiceImpl());
    }

}
