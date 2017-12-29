package de.vkoop.monit.checks;


import de.vkoop.monit.checks.result.HealthCheck;

/**
 * Health check with name.
 */
public interface NamedHealthCheck extends HealthCheck {

    String getName();

}
