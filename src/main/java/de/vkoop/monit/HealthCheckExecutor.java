package de.vkoop.monit;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.reactivex.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.SortedMap;

@Component
public class HealthCheckExecutor {

    @Autowired
    HealthCheckRegistry healthCheckRegistry;

    @Autowired
    Subject<Map<String, HealthCheck.Result>> checkSubject;

    @Scheduled(fixedRateString = "${health.checkrate}")
    public void check() {
        SortedMap<String, HealthCheck.Result> entries = healthCheckRegistry.runHealthChecks();
        checkSubject.onNext(entries);
    }
}