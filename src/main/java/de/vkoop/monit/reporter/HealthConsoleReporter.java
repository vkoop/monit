package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.stereotype.Component;

import java.util.SortedMap;

@Component
public class HealthConsoleReporter implements HealthReporter {

    @Override
    public void reportAll(SortedMap<String, HealthCheck.Result> results) {
        System.out.println(results);
    }
}
