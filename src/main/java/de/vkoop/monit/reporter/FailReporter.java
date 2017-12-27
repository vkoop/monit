package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.filter.Filter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface FailReporter extends HealthReporter {

    Filter<String> getFilter();

    Observable<Tuple2<String, HealthCheck.Result>> getObservable();

    default Observable<Tuple2<String, HealthCheck.Result>> getFailObservable() {
        return getObservable()
                .filter(tuple -> !tuple._2.isHealthy());
    }

    @PostConstruct
    default void registerFailReporter() {
        Filter<String> filter = getFilter();

        getFailObservable()
                .filter(tuple -> filter.canPass(tuple._1))
                .subscribeOn(getScheduler())
                .subscribe(this::reportSingle);
    }

    Scheduler getScheduler();
}
