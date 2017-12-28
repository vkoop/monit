package de.vkoop.monit.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;

public abstract class OsCondition implements Condition {
    protected String getOsProperty(ConditionContext context) {
        return context.getEnvironment().getProperty("os.name").toLowerCase();
    }
}
