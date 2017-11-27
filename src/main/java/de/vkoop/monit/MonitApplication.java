package de.vkoop.monit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableScheduling
@SpringBootApplication
public class MonitApplication {

    public static void main(String[] args) {
        //TODO move to right place
        ApiContextInitializer.init();

        SpringApplication.run(MonitApplication.class, args);
    }
}
