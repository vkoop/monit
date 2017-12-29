package de.vkoop.monit.reporter.impl;

import de.vkoop.monit.checks.Result;
import de.vkoop.monit.filter.StatefulFilter;
import de.vkoop.monit.properties.MailProperties;
import de.vkoop.monit.reporter.RestoreableFailReporter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.stream.Collectors;


@Slf4j
@Profile("mail")
@Component("healthMailReporter")
public class HealthMailReporter implements RestoreableFailReporter {

    private static final String DELIMITER = "\n";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailConfig;

    @Autowired
    private Observable<Tuple2<String, Result>> checkObservableHot;

    @Autowired
    private StatefulFilter<String> alreadyReportedItemsFilter;

    @Named("ioScheduler")
    @Autowired
    private Scheduler ioScheduler;

    @Override
    public void reportAll(Map<String, Result> results) {
        String unhealthyEntries = results
                .map(Object::toString)
                .collect(Collectors.joining(DELIMITER));

        sendMail(unhealthyEntries);
    }

    @Override
    public void reportSingle(Tuple2<String, Result> resultTuple) {
        log.debug("sending mail {}", resultTuple._1);

        sendMail(resultTuple.toString());
    }

    @Override
    public void onRestore(String key) {
        sendMail("restored: " + key);
    }

    private void sendMail(String mailText) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(mailConfig.fromMail);
            mimeMessageHelper.setTo(mailConfig.toEmail);
            mimeMessageHelper.setSubject("Report");
            mimeMessageHelper.setText(mailText, false);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("error occured while sending message", e);
        }
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
    public Scheduler getScheduler() {
        return ioScheduler;
    }
}
