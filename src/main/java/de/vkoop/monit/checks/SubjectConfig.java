package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
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
    public Subject<Map<String, HealthCheck.Result>> checkSubject() {
        Subject<Map<String, HealthCheck.Result>> subject = PublishSubject.create();
        subject.publish();

        return subject;
    }

    @Scope("singleton")
    @Bean
    public Observable<Map<String, HealthCheck.Result>> windowedCheckSubject(Subject<Map<String, HealthCheck.Result>> checkSubject) {
        Observable<Map<String, HealthCheck.Result>> hashMapObservable = checkSubject.window(30, TimeUnit.MINUTES).
                flatMap(obs -> obs.reduce(new HashMap<String, HealthCheck.Result>(), (accum, curr) -> {
                    accum.putAll(curr);
                    return accum;
                }).toObservable());

        return hashMapObservable;
    }
}
