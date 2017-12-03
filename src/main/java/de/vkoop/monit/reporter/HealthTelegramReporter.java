package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Profile("telegram")
@Component
public class HealthTelegramReporter extends TelegramLongPollingBot implements HealthReporter {

    @Autowired
    Observable<Tuple2<String, HealthCheck.Result>> unhealthyThrottled;

    @Value("#{ '${telegram.recipients}'.split(',') }")
    List<Long> recipients;

    @Value("${telegram.authtoken}")
    private String authToken;

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

        sendMessageToAll(collect);
    }

    @Override
    public void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple) {
        String text = resultTuple.toString();
        sendMessageToAll(text);
    }

    private void sendMessageToAll(String text) {
        for (Long recipient : recipients) {
            sendMessage(text, recipient);
        }
    }

    private void sendMessage(String text, long chatId) {
        log.debug("sending message to chat {}", chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);

        //TODO move to config
        sendMessage.setChatId(chatId);

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
        return authToken;
    }

    @Override
    public void clearWebhook() {
    }
}
