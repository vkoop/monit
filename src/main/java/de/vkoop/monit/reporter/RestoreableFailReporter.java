package de.vkoop.monit.reporter;

import de.vkoop.monit.checks.Result;
import de.vkoop.monit.filter.StatefulFilter;
import io.vavr.Tuple2;

import javax.annotation.PostConstruct;

public interface RestoreableFailReporter extends FailReporter {

    @Override
    StatefulFilter<String> getFilter();

    @PostConstruct
    default void registerRestoreAction() {
        getObservable()
                .filter(this::checkIfRestored)
                .subscribeOn(getScheduler())
                .map(t -> t._1)
                .subscribe(this::onRestore);
    }

    default boolean checkIfRestored(Tuple2<String, Result> tuple) {
        boolean healthy = tuple._2.isHealthy();
        String name = tuple._1;
        return healthy && getFilter().restore(name);
    }

    void onRestore(String key);
}
