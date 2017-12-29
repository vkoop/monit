package de.vkoop.monit.checks.impl;

import de.vkoop.monit.checks.Result;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebsiteUpCheckTest {

    @Mock
    private HttpClient httpClient;

    @Test
    public void testSuccess() throws IOException {
        HttpResponse value = status200();
        when(httpClient.execute(any())).thenReturn(value);

        WebsiteUpCheck check = new WebsiteUpCheck("websiteA", "http://myawesomesite.de", httpClient);
        Result result = check.check();

        assertTrue(result.isHealthy());
    }

    @Test
    public void testFailingLookup() throws IOException {
        HttpResponse value = status500();
        when(httpClient.execute(any())).thenReturn(value);

        WebsiteUpCheck check = new WebsiteUpCheck("websiteA", "http://myawesomesite.de", httpClient);
        Result result = check.check();

        assertFalse(result.isHealthy());
    }

    private HttpResponse status200() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, ""));

        return response;
    }

    private HttpResponse status500() {
        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 500, ""));

        return response;
    }

}