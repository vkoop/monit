package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class HealthMailReporter implements HealthReporter {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailConfig mailConfig;

    @Autowired
    Subject<Map<String, HealthCheck.Result>> checkSubject;

    @PostConstruct
    public void onInit() {
        Observable<HashMap<String, HealthCheck.Result>> hashMapObservable = checkSubject.window(30, TimeUnit.MINUTES).
                flatMap(obs -> obs.reduce(new HashMap<String, HealthCheck.Result>(), (accum, curr) -> {
                    accum.putAll(curr);
                    return accum;
                }).toObservable());

        hashMapObservable.subscribeOn(Schedulers.io())
                .subscribe(this::reportAll);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        String unhealthyEntries = results.entrySet().stream()
                .filter(entry -> !entry.getValue().isHealthy())
                .map(entry -> entry.getKey() + " failed")
                .collect(Collectors.joining("\n"));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setFrom(mailConfig.fromMail);
            mimeMessageHelper.setTo(mailConfig.toEmail);
            mimeMessageHelper.setSubject("Report");
            mimeMessageHelper.setText(unhealthyEntries, false);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
