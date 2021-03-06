package de.vkoop.monit.reporter.impl;

import de.vkoop.monit.checks.Result;
import de.vkoop.monit.config.ReporterIntegrationTestConfig;
import io.reactivex.schedulers.TestScheduler;
import io.vavr.Tuple2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"test", "mail"})
@SpringBootTest
public class HealthMailReporterTest {

    @Resource(name = "testHealthMailReporter")
    private HealthMailReporter reporter;

    @Captor
    private ArgumentCaptor<Tuple2<String, Result>> captor;

    @Test
    public void testReportSingle() {
        TestScheduler scheduler = (TestScheduler) reporter.getScheduler();
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        verify(reporter, times(1)).reportSingle(captor.capture());
        String errorKey = captor.getValue()._1;
        assertEquals("key2", errorKey);
    }

    @Import(ReporterIntegrationTestConfig.class)
    @TestConfiguration
    public static class MyTestConfiguration {

        @Primary
        @Bean
        JavaMailSender getMailSender() {
            JavaMailSender mailSenderMock = mock(JavaMailSender.class);
            when(mailSenderMock.createMimeMessage()).thenReturn(mock(MimeMessage.class));
            return mailSenderMock;
        }

        @Bean("testHealthMailReporter")
        HealthMailReporter getReporter(HealthMailReporter healthMailReporter) {
            return Mockito.spy(healthMailReporter);
        }


    }
}