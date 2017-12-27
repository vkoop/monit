package de.vkoop.monit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Monit application starter
 */
@EnableScheduling
@SpringBootApplication
public class MonitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitApplication.class, args);
    }
}
