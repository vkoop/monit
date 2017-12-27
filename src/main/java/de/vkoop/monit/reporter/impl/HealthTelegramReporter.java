package de.vkoop.monit.reporter.impl;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.filter.StatefulFilter;
import de.vkoop.monit.reporter.RestoreableFailReporter;
import io.reactivex.Observable;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Profile("telegram")
@Component
public class HealthTelegramReporter extends TelegramLongPollingBot implements RestoreableFailReporter {

    @Autowired
    Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot;

    @Value("#{ '${telegram.recipients}'.split(',') }")
    List<Long> recipients;

    @Value("${telegram.authtoken}")
    private String authToken;

    @Autowired
    StatefulFilter<String> alreadyReportedItemsFilter;

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

    @Override
    public StatefulFilter<String> getFilter() {
        return alreadyReportedItemsFilter;
    }

    @Override
    public Observable<Tuple2<String, HealthCheck.Result>> getObservable() {
        return checkObservableHot;
    }

    @Override
    public void onRestore(String key) {
        sendMessageToAll("Restored: " + key);
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
        sendMessage.setChatId(chatId);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message", e);
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