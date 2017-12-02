package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vavr.Tuple2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class SubjectConfig {

    @Scope("singleton")
    @Bean
    public Subject<Tuple2<String, HealthCheck.Result>> checkSubject() {
        return PublishSubject.create();
    }

    @Scope("singleton")
    @Bean
    public Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot(Subject<Tuple2<String, HealthCheck.Result>> checkSubject) {
        return checkSubject.publish();
    }

    @Scope("singleton")
    @Bean
    public Observable<Map<String, HealthCheck.Result>> windowedCheckSubject(Observable<Tuple2<String, HealthCheck.Result>> checkSubject) {
        return checkSubject.window(30, TimeUnit.SECONDS).
                flatMap(obs -> obs.reduce(new HashMap<String, HealthCheck.Result>(), (accum, curr) -> {
                    accum.put(curr._1, curr._2);
                    return accum;
                }).toObservable());
    }

    @Scope("singleton")
    @Bean
    public Observable<Map<String, HealthCheck.Result>> windowedUnhealthy(Observable<Tuple2<String, HealthCheck.Result>> checkSubject){
        Observable<Tuple2<String, HealthCheck.Result>> filteredObs = checkSubject.filter(tuple -> !tuple._2.isHealthy());
        return windowedCheckSubject(filteredObs);
    }
}
