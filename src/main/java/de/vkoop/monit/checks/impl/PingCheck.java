package de.vkoop.monit.checks.impl;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.Result;
import de.vkoop.monit.conditions.LinuxCondition;
import de.vkoop.monit.conditions.WindowsCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Health check to check if ip is reachable.
 */
@Slf4j
@RequiredArgsConstructor
public class PingCheck implements NamedHealthCheck {

    private final String ip;
    private final PingCommandStrategy pingCommandStrategy;
    private final String name;

    @Override
    public Result check() {
        List<String> command = pingCommandStrategy.getPingCommand(ip);

        ProcessBuilder ping = new ProcessBuilder().command(command);
        try {
            Process start = ping.start();
            start.waitFor();
            int returnValue = start.exitValue();

            if (returnValue == 0) {
                return Result.healthy();
            } else {
                return Result.unhealthy("Invalid ping");
            }

        } catch (IOException | InterruptedException e) {
            String msg = "Ping failed with exception.";
            log.error(msg, e);
            return Result.unhealthy(msg);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public interface PingCommandStrategy {
        List<String> getPingCommand(String ip);
    }

    @Conditional(LinuxCondition.class)
    @Component
    public static class LinuxPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getPingCommand(String ip) {
            return Arrays.asList("ping", "-c1", ip);
        }
    }

    @Conditional(WindowsCondition.class)
    @Component
    public static class WindowsPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getPingCommand(String ip) {
            return Arrays.asList("ping", "-n", "1", ip);
        }
    }

}
