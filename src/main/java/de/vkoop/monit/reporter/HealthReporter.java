package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;

import java.util.SortedMap;

public interface HealthReporter {

    void reportAll(SortedMap<String, HealthCheck.Result> results);


}
