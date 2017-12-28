package de.vkoop.monit.reporter.impl;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test,mail,windows")
@SpringBootTest
@TestPropertySource(properties = {"spring.mail.host = localhost", "spring.mail.port: 3025"})
public class HealthIntegrationTest {

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

    @Resource(name = "healthMailReporter")
    private HealthMailReporter reporter;

    @Test
    public void testSendMail() {
        TestScheduler scheduler = (TestScheduler) reporter.getScheduler();
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
    }

    @Import(ReporterIntegrationTestConfig.class)
    @TestConfiguration
    public static class MyTestConfiguration {
    }
}