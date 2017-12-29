package de.vkoop.monit.checks.result;

import de.vkoop.monit.checks.Result;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class HealthyResult extends Result {

    public static final HealthyResult HEALTHY = new HealthyResult();

    @Override
    public boolean isHealthy() {
        return true;
    }
}
