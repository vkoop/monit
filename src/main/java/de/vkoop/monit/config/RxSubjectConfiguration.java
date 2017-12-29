package de.vkoop.monit.config;

import de.vkoop.monit.checks.Result;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class RxSubjectConfiguration {

    @Scope("singleton")
    @Bean
    public Subject<Tuple2<String, Result>> checkSubject() {
        return PublishSubject.create();
    }

    @Scope("singleton")
    @Bean
    public Observable<Tuple2<String, Result>> checkObservableHot(Subject<Tuple2<String, Result>> checkSubject) {
        ConnectableObservable<Tuple2<String, Result>> publishedObservable = checkSubject.publish();
        publishedObservable.connect();

        return publishedObservable;
    }

}
