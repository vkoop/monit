package de.vkoop.monit.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "monit")
public class AppProperties {

    public int checkrate = 5000;
    public int reportRate = 12;
    public List<CheckTargetProperties> hosts = new ArrayList<>();
    public MailProperties mailconfig;
    public TelegramProperties telegram;

    public List<WebsiteTestProperties> websiteTests = new ArrayList<>();

    @Data
    public static class WebsiteTestProperties {
        public String url;
        public String name;
    }

    @Data
    public static class CheckTargetProperties {
        public int port;
        public String ip;
        public String name;
    }

    @Data
    public static class TelegramProperties {
        public String authtoken;
        public List<Integer> recipients;
    }
}
