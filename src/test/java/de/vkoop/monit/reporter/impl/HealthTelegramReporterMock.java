package de.vkoop.monit.reporter.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Component("healthTelegramReporter")
@Profile("mock-telegram")
public class HealthTelegramReporterMock extends HealthTelegramReporter {

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        return null;
    }
}
