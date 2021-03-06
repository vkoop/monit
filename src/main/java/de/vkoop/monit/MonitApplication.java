package de.vkoop.monit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

/**
 * Monit application starter
 */
@EnableScheduling
@SpringBootApplication
public class MonitApplication {

    public static void main(final String[] args) {
        ApiContextInitializer.init();

        SpringApplication.run(MonitApplication.class, args);
    }
}
