package de.vkoop.monit.reporter.impl;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ReporterIntegrationTestConfig {

    @Bean("ioScheduler")
    public Scheduler getIoScheduler() {
        return new TestScheduler();
    }

    @Bean
    public Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot() {
        Tuple2<String, HealthCheck.Result> healthy = Tuple.of("key1", HealthCheck.Result.healthy());
        Tuple2<String, HealthCheck.Result> unhealthy = Tuple.of("key2", HealthCheck.Result.unhealthy("blub"));

        return Observable.fromArray(healthy, unhealthy);
    }

}
