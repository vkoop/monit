package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.Filter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface FailReporter extends HealthReporter {

    Observable<Tuple2<String, HealthCheck.Result>> getObservable();

    Filter<String> getFilter();

    @PostConstruct
    default void registerFailReporter() {
        getObservable()
                .filter(tuple -> !tuple._2.isHealthy())
                .filter(tuple -> getFilter().filter(tuple._1))
                .subscribeOn(Schedulers.io())
                .subscribe(this::reportSingle);
    }
}
