package de.vkoop.monit.checks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class WebsiteUpTest extends NamedHealthCheck {

    public final String name;
    public final String url;
    private final HttpClient httpClient;

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected Result check() {
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse execute = httpClient.execute(httpGet);
            StatusLine statusline = execute.getStatusLine();
            int statusCode = statusline.getStatusCode();

            httpGet.releaseConnection();

            if (statusCode != 200) {
                return Result.unhealthy("Request responded with status code %s", statusCode);
            } else {
                return Result.healthy();
            }

        } catch (IOException e) {
            return Result.unhealthy("Website check failed", e);
        }
    }
}
