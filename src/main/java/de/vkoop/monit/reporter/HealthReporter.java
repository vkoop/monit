package de.vkoop.monit.reporter;


import de.vkoop.monit.checks.Result;
import io.vavr.Tuple2;
import io.vavr.collection.Map;


/**
 * Report health issues to given channel.
 */
public interface HealthReporter {
    void reportAll(Map<String, Result> results);

    void reportSingle(Tuple2<String, Result> resultTuple);

    void onRestore(String key);
}
