package ist.sec.coin.client.it;

import ist.sec.coin.server.ws.EchoException_Exception;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.ws.WebServiceException;

public class EchoTest extends BaseServiceIT {

    @Test
    public void testReturns() throws EchoException_Exception {
        String response = client.echo("Hello");
        Assert.assertNotNull(response);
    }

    @Test
    public void testReturnsSameMessage() throws EchoException_Exception {
        String message = "qwerty";
        String response = client.echo(message);
        Assert.assertTrue(response.contains(message));
    }

    @Test(expected = WebServiceException.class)
    public void testNullEchoMessage() throws EchoException_Exception {
        client.echo(null);
    }

    @Test(expected = EchoException_Exception.class)
    public void testEmptyEchoMessage() throws EchoException_Exception {
        client.echo("");
    }
}
