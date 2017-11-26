package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.subjects.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class HealthConsoleReporter implements HealthReporter {

    @Autowired
    Subject<Map<String, HealthCheck.Result>> checkSubject;

    @PostConstruct
    public void onInit() {
        checkSubject.subscribe(this::reportAll);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        System.out.println(results);
    }
}
