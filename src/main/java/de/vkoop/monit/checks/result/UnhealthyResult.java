package de.vkoop.monit.checks.result;

import de.vkoop.monit.checks.Result;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper=false)
@Builder
public class UnhealthyResult extends Result {

    private String message;

    private Throwable exception;

    @Override
    public boolean isHealthy() {
        return false;
    }
}
