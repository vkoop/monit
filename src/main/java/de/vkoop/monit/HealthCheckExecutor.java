package de.vkoop.monit;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import de.vkoop.monit.reporter.HealthReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.SortedMap;

@Component
public class HealthCheckExecutor {

    @Autowired
    HealthCheckRegistry healthCheckRegistry;

    @Autowired
    List<HealthReporter> reporters;

    @Scheduled(fixedRateString = "${health.checkrate}")
    public void check() {
        SortedMap<String, HealthCheck.Result> entries = healthCheckRegistry.runHealthChecks();

        for (HealthReporter reporter : reporters) {
            reporter.reportAll(entries);
        }
    }
}