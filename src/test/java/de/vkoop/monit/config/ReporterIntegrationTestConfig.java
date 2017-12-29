package de.vkoop.monit.config;

import de.vkoop.monit.checks.Result;
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
    public Observable<Tuple2<String, Result>> checkObservableHot() {
        Tuple2<String, Result> healthy = Tuple.of("key1", Result.healthy());
        Tuple2<String, Result> unhealthy = Tuple.of("key2", Result.unhealthy("blub"));

        return Observable.fromArray(healthy, unhealthy);
    }

}
