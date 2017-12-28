package de.vkoop.monit.reporter.impl;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.reporter.HealthReporter;
import io.reactivex.Observable;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Slf4j
@Profile("console")
@Component
public class HealthConsoleReporter implements HealthReporter {

    @Autowired
    private Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot;

    @PostConstruct
    public void onInit() {
        checkObservableHot.subscribe(this::reportSingle);
    }

    @Override
    public void reportAll(Map<String, HealthCheck.Result> results) {
        log.info("Report all: {}", results);
    }

    @Override
    public void reportSingle(Tuple2<String, HealthCheck.Result> resultTuple) {
        log.info("Report single: {}", resultTuple);
    }

    @Override
    public void onRestore(String key) {
        log.info("Report key: {}", key);
    }
}
