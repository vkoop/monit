package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;

import java.util.Map;

public interface HealthReporter {
    void reportAll(Map<String, HealthCheck.Result> results);
}
