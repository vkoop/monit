package de.vkoop.monit.reporter.impl;

import com.codahale.metrics.health.HealthCheck;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ActiveProfiles("test,mock-telegram")
@SpringBootTest
public class HealthTelegramReporterTest {

    @Resource(name = "testHealthTelegramReporter")
    private HealthTelegramReporter reporter;

    @Captor
    private ArgumentCaptor<Tuple2<String, HealthCheck.Result>> captor;

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

        @Bean("testHealthTelegramReporter")
        HealthTelegramReporter getReporter(HealthTelegramReporter healthTelegramReporter) {
            return Mockito.spy(healthTelegramReporter);
        }
    }
}