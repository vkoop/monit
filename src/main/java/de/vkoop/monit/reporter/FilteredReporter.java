package de.vkoop.monit.reporter;

import com.codahale.metrics.health.HealthCheck;
import de.vkoop.monit.StatefulFilter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface FilteredReporter {

    StatefulFilter<String> getFilter();

    Observable<Tuple2<String, HealthCheck.Result>> getObservable();

    @PostConstruct
    default void registerRestore() {
        getObservable()
                .filter(t -> t._2.isHealthy())
                .filter(this::checkIfRestored)
                .subscribeOn(Schedulers.io())
                .map(t -> t._1)
                .subscribe(this::onRestore);
    }

    default boolean checkIfRestored(Tuple2<String, HealthCheck.Result> tuple) {
        boolean healthy = tuple._2.isHealthy();
        String name = tuple._1;
        return healthy && getFilter().restore(name);
    }

    void onRestore(String key);
}
