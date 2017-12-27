package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.filter.Filter;
import de.vkoop.monit.filter.StatefulFilter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface FailReporter extends HealthReporter {

    Filter<String> getFilter();

    Observable<Tuple2<String, HealthCheck.Result>> getObservable();

    default Observable<Tuple2<String, HealthCheck.Result>> getFailObservable(){
        return getObservable()
                .filter(tuple -> !tuple._2.isHealthy());
    }

    @PostConstruct
    default void registerFailReporter() {
        Filter<String> filter = getFilter();

        getFailObservable()
                .filter(tuple -> filter.canPass(tuple._1))
                .subscribeOn(Schedulers.io())
                .subscribe(this::reportSingle);
    }
}
