package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.Filter;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vavr.Tuple2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
    public Observable<Tuple2<String, HealthCheck.Result>> unhealthyThrottled(Observable<Tuple2<String, HealthCheck.Result>> checkSubject, Filter<String> filter) {
        return checkSubject.filter(i -> filter.filter(i._1))
                .filter(tuple -> !tuple._2.isHealthy());
    }
}
