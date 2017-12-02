package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("mail")
@Component
public class HealthMailReporter implements HealthReporter {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailProperties mailConfig;

    @Autowired
    Observable<Tuple2<String, HealthCheck.Result>> unhealthyThrottled;

    @PostConstruct
    public void onInit() {
        unhealthyThrottled.subscribeOn(Schedulers.io())
                .subscribe(this::reportSingle);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        String unhealthyEntries = results.entrySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        sendMail(unhealthyEntries);
    }

    @Override
    public void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple) {
        sendMail(resultTuple.toString());
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
            e.printStackTrace();
        }
    }
}
