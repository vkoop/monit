package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class HealthConsoleReporter implements HealthReporter {

    @Override
    public void reportAll(Set<Map.Entry<String, HealthCheck.Result>> results) {
        System.out.println(results);
    }
}
