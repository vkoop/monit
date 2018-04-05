package de.vkoop.monit.config;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.result.HealthCheck;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class HealthConfiguration {

    @Bean
    public List<NamedHealthCheck> allChecks(java.util.List<List<NamedHealthCheck>> allCheckLists) {
        return Stream.ofAll(allCheckLists)
                .foldLeft(List.empty(), List::prependAll);
    }

    @Bean
    public Map<String, HealthCheck> healthChecks(List<NamedHealthCheck> allChecks) {
        return allChecks
                .toMap(NamedHealthCheck::getName, Function.identity());
    }
}
