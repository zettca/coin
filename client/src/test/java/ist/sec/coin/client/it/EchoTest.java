package ist.sec.coin.client.it;

import org.junit.Assert;
import org.junit.Test;

public class EchoTest extends BaseServiceIT {

    @Test
    public void testReturns() {
        String response = client.echo("Hello");
        Assert.assertNotNull(response);
    }

    @Test
    public void testReturnsSameMessage() {
        String message = "qwerty";
        String response = client.echo(message);
        Assert.assertTrue(response.contains(message));
    }
}
