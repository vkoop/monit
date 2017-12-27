package de.vkoop.monit.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RxConfiguration {

    @Bean("ioScheduler")
    public Scheduler getIoScheduler() {
        return Schedulers.io();
    }

}
