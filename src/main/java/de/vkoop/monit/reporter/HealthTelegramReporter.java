package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("telegram")
@Component
public class HealthTelegramReporter extends TelegramLongPollingBot implements HealthReporter {

    @Autowired
    Observable<Tuple2<String, HealthCheck.Result>> unhealthyThrottled;

    @PostConstruct
    public void onInit() {
        unhealthyThrottled.subscribeOn(Schedulers.io())
                .subscribe(this::reportSingle);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        String collect = results.entrySet().stream()
                .map(tuple -> tuple.getKey() + " unhealthy")
                .collect(Collectors.joining("\n"));

        sendMessage(collect);
    }

    @Override
    public void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple) {
        String text = resultTuple.toString();
        sendMessage(text);
    }

    private void sendMessage(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);

        //TODO move to config
        sendMessage.setChatId(9464296L);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            //TODO
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
    public void clearWebhook() {
    }
}
