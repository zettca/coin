package ist.sec.coin.client.it;

import org.junit.Assert;
import org.junit.Test;

public class EchoTest extends BaseServiceIT {

    @Test
    public void testReturn() {
        String response = client.echo("Hello");
        Assert.assertNotNull(response);
    }

    @Test
    public void testIsEcho() {
        String message = "qwerty";
        String response = client.echo(message);
        Assert.assertTrue(response.contains(message));
    }
}
