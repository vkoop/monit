package de.vkoop.monit.checks;


import de.vkoop.monit.checks.result.HealthCheck;
import io.reactivex.subjects.Subject;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Executes all health checks contained
 * in the health check registry.
 */
@Component
public class HealthCheckExecutor {

    @Autowired
    Map<String, HealthCheck> healthChecks;

    @Autowired
    private Subject<Tuple2<String, Result>> checkSubject;

    @Scheduled(fixedRateString = "${monit.checkrate}")
    public void check() {
        Seq<Tuple2<String, Result>> results = healthChecks
                .map(e -> e.map2(HealthCheck::check));

        results.forEach(checkSubject::onNext);
    }
}