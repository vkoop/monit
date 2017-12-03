package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.Filter;
import io.reactivex.Observable;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Profile("mail")
@Component
public class HealthMailReporter implements HealthReporter, FilteredReporter, FailReporter {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailProperties mailConfig;

    @Autowired
    Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot;

    @Autowired
    Filter<String> filterByName;

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        String unhealthyEntries = results.entrySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        sendMail(unhealthyEntries);
    }

    @Override
    public void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple) {
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
            e.printStackTrace();
        }
    }

    @Override
    public Filter<String> getFilter() {
        return filterByName;
    }

    @Override
    public Observable<Tuple2<String, HealthCheck.Result>> getObservable() {
        return checkObservableHot;
    }
}
