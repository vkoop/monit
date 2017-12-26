package de.vkoop.monit.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties
public class AppProperties {

    public List<MyConfigEntry> myhosts;

    @Data
    public static class MyConfigEntry {
        public int port;
        public String ip;
        public String name;
    }
}
