package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HealthMailReporter implements HealthReporter {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    MailConfig mailConfig;

    @Override
    public void reportAll(Set<Map.Entry<String, HealthCheck.Result>> results) {

        String unhealthyEntries = results.stream()
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
