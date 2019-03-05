package de.vkoop.monit.reporter.impl;

import de.vkoop.monit.checks.Result;
import de.vkoop.monit.filter.StatefulFilter;
import de.vkoop.monit.reporter.RestoreableFailReporter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Named;
import java.util.stream.Collectors;

@Slf4j
@Profile("telegram")
@Component("healthTelegramReporter")
public class HealthTelegramReporter extends TelegramLongPollingBot implements RestoreableFailReporter {

    @Autowired
    private Observable<Tuple2<String, Result>> checkObservableHot;

    @Value("#{ '${monit.telegram.recipients}'.split(',') }")
    private java.util.List<Long> recipients;

    @Value("${monit.telegram.authtoken}")
    private String authToken;

    @Value("${monit.telegram.botname}")
    private String botname;

    @Autowired
    private StatefulFilter<String> alreadyReportedItemsFilter;

    @Named("ioScheduler")
    @Autowired
    @Getter
    private Scheduler scheduler;

    @Override
    public void reportAll(Map<String, Result> results) {
        String collect = results
                .map(tuple -> tuple._1() + " unhealthy")
                .collect(Collectors.joining("\n"));

        sendMessageToAll(collect);
    }

    @Override
    public void reportSingle(Tuple2<String, Result> resultTuple) {
        String text = resultTuple.toString();
        sendMessageToAll(text);
    }

    @Override
    public StatefulFilter<String> getFilter() {
        return alreadyReportedItemsFilter;
    }

    @Override
    public Observable<Tuple2<String, Result>> getObservable() {
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
        return botname;
    }

    @Override
    public String getBotToken() {
        return authToken;
    }

    @Override
    public void clearWebhook() {
    }

}
