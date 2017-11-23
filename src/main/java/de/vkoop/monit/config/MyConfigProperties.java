package de.vkoop.monit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties
public class MyConfigProperties {
    public List<MyConfigEntry> myhosts;

    public void setMyhosts(List<MyConfigEntry> myhosts) {
        this.myhosts = myhosts;
    }

    public List<MyConfigEntry> getMyhosts() {
        return myhosts;
    }

    public static class MyConfigEntry {

        public int port;
        public String ip;
        public String name;


        public void setName(String name) {
            this.name = name;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
