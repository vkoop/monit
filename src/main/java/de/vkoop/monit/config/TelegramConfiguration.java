package de.vkoop.monit.config;

import de.vkoop.monit.reporter.impl.HealthTelegramReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Profile("telegram")
@Configuration
public class TelegramConfiguration {

    @PostConstruct
    public void init() {
        ApiContextInitializer.init();
    }

    @Scope("singleton")
    @Bean
    public TelegramBotsApi getTelegramBotsApi(HealthTelegramReporter healthTelegramReporter) throws TelegramApiRequestException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(healthTelegramReporter);

        return telegramBotsApi;
    }
}
