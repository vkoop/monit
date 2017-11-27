package de.vkoop.monit.config;

import de.vkoop.monit.reporter.HealthTelegramReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@Profile("telegram")
@Configuration
public class TelegramConfiguration {

    @Scope("singleton")
    @Bean
    public TelegramBotsApi getTelegramBotsApi(HealthTelegramReporter telegramReporter) throws TelegramApiRequestException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(telegramReporter);

        return telegramBotsApi;
    }

}
