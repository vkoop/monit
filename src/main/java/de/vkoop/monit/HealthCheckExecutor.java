package de.vkoop.monit;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import de.vkoop.monit.reporter.HealthReporter;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

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