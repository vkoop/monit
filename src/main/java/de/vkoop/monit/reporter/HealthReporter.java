package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.vavr.Tuple2;

import java.util.Map;

public interface HealthReporter {
    void reportAll(Map<String, HealthCheck.Result> results);

    void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple);
}
