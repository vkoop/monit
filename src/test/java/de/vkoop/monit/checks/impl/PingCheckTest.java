package de.vkoop.monit.checks.impl;

import de.vkoop.monit.checks.Result;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PingCheckTest {

    private PingCheck pingCheck;

    @Mock
    private PingCheck.PingCommandStrategy pingCommandStrategy;

    @Mock
    private Process successfulProcess;

    @Mock
    private Process failingProcess;

    @Before
    public void setUp() throws Exception {
        pingCheck = spy(new PingCheck("127.0.0.1", pingCommandStrategy, "test-name"));

        when(failingProcess.exitValue()).thenReturn(-1);
    }

    @Test
    public void testSuccess() throws IOException {
        doReturn(successfulProcess).when(pingCheck).createProcessBuilder(anyList());

        final Result check = pingCheck.check();

        assertTrue(check.isHealthy());
    }

    @Test
    public void testFail() throws IOException {
        doReturn(failingProcess).when(pingCheck).createProcessBuilder(anyList());

        final Result check = pingCheck.check();

        assertFalse(check.isHealthy());
    }
}
