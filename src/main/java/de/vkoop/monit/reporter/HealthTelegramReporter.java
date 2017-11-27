package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Profile("telegram")
@Component
public class HealthTelegramReporter extends TelegramLongPollingBot implements HealthReporter {

    @Autowired
    Observable<Map<String, HealthCheck.Result>> windowedCheckSubject;

    @PostConstruct
    public void onInit() {
        windowedCheckSubject.subscribeOn(Schedulers.io())
                .subscribe(this::reportAll);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        String collect = results.entrySet().stream()
                .filter(x -> !x.getValue().isHealthy())
                .map(x -> x.getKey() + " unhealthy")
                .collect(Collectors.joining("\n"));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(collect);

        //TODO move to config
        sendMessage.setChatId(9464296L);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return "efbmon_bot";
    }

    @Override
    public String getBotToken() {
        return "409312434:AAGZsRDk7sqiVZnyeLIruj6wTJk5f7uAKA8";
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }
}
