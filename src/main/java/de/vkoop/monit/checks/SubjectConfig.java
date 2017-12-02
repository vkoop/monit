package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.Filter;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
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
        ConnectableObservable<Tuple2<String, HealthCheck.Result>> publish = checkSubject.publish();
        publish.connect();

        return publish;
    }

    @Scope("prototype")
    @Bean
    public Observable<Tuple2<String, HealthCheck.Result>> unhealthyThrottled(Observable<Tuple2<String, HealthCheck.Result>> checkObservableHot, Filter<String> filter) {
        return checkObservableHot
                .filter(tuple -> !tuple._2.isHealthy())
                .filter(i -> filter.filter(i._1));
    }
}
