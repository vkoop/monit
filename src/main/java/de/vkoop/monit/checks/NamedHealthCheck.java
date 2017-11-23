package de.vkoop.monit.checks;

import com.codahale.metrics.health.HealthCheck;

public abstract class NamedHealthCheck extends HealthCheck {

    public abstract String getName();
}
