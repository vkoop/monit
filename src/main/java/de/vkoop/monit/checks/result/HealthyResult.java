package de.vkoop.monit.checks.result;

import de.vkoop.monit.checks.Result;

public class HealthyResult extends Result {

    public static final HealthyResult HEALTHY = new HealthyResult();

    @Override
    public boolean isHealthy() {
        return true;
    }
}
