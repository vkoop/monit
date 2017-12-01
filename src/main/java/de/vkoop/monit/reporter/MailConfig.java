package de.vkoop.monit.reporter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties("mailconfig")
public class MailConfig {
    String fromMail;
    public String toEmail;
}
