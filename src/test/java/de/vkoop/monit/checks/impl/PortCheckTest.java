package de.vkoop.monit.checks.impl;

import de.vkoop.monit.checks.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class PortCheckTest {

    private PortCheck check = Mockito.spy(new PortCheck(1, "invalidhost", "name"));

    @Mock
    private Socket socket;

    @Test
    public void testSuccess() throws IOException {
        doReturn(socket).when(check).createSocket();

        final Result result = this.check.check();
        assertTrue(result.isHealthy());
    }

    @Test
    public void testFailure() throws IOException {
        doThrow(new UnknownHostException()).when(check).createSocket();

        final Result result = this.check.check();
        assertFalse(result.isHealthy());
    }
}
