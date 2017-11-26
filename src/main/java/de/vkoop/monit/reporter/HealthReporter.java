package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;

import java.util.Map;
import java.util.Set;

public interface HealthReporter {

    void reportAll(Set<Map.Entry<String, HealthCheck.Result>> results);


}
