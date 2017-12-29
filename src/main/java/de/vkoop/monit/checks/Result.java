package de.vkoop.monit.checks;

import de.vkoop.monit.checks.result.HealthyResult;
import de.vkoop.monit.checks.result.UnhealthyResult;

public abstract class Result {

    public static Result healthy() {
        return HealthyResult.HEALTHY;
    }

    public static Result unhealthy(String message) {
        return UnhealthyResult.builder().message(message).build();
    }

    public static Result unhealthy(Throwable e) {
        return UnhealthyResult.builder().exception(e).build();
    }

    public static Result unhealthy(String message, Throwable e) {
        return UnhealthyResult.builder().message(message).exception(e).build();
    }

    public static Result unhealthy(String message, Object... args) {
        String format = String.format(message, args);
        return UnhealthyResult.builder().message(format).build();
    }

    public abstract boolean isHealthy();
}
