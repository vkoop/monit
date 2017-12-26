package de.vkoop.monit;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.reactivex.subjects.Subject;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.SortedMap;

/**
 * Executes all health checks contained
 * in the health check registry.
 */
@Component
public class HealthCheckExecutor {

    @Autowired
    HealthCheckRegistry healthCheckRegistry;

    @Autowired
    Subject<Tuple2<String, HealthCheck.Result>> checkSubject;

    @Scheduled(fixedRateString = "${health.checkrate}")
    public void check() {
        SortedMap<String, HealthCheck.Result> entries = healthCheckRegistry.runHealthChecks();

        entries.forEach((key, value) -> {
            Tuple2<String, HealthCheck.Result> tuple = Tuple.of(key, value);
            checkSubject.onNext(tuple);
        });
    }
}