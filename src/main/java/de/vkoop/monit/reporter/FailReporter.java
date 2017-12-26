package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.StatefulFilter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface FailReporter extends HealthReporter {

    Observable<Tuple2<String, HealthCheck.Result>> getObservable();

    StatefulFilter<String> getFilter();

    @PostConstruct
    default void registerFailReporter() {
        StatefulFilter<String> filter = getFilter();

        getObservable()
                .filter(tuple -> !tuple._2.isHealthy())
                .filter(tuple -> filter.blockAndCheckIfNew(tuple._1))
                .subscribeOn(Schedulers.io())
                .subscribe(this::reportSingle);
    }
}
