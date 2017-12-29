package de.vkoop.monit.checks.result;

import de.vkoop.monit.checks.Result;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
@Builder
public class UnhealthyResult extends Result {

    private String message;

    private Throwable exception;

    @Override
    public boolean isHealthy() {
        return false;
    }
}
