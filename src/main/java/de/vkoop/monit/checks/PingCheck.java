package de.vkoop.monit.checks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Health check to check if ip is reachable.
 */
@Slf4j
@RequiredArgsConstructor
public class PingCheck extends NamedHealthCheck {

    private final String ip;
    private final PingCommandStrategy pingCommandStrategy;
    private final String name;

    @Override
    protected Result check() {
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

    @Profile("linux")
    @Component
    public static class LinuxPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getPingCommand(String ip) {
            return Arrays.asList("ping", "-c1", ip);
        }
    }

    @Profile("windows")
    @Component
    public static class WindowsPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getPingCommand(String ip) {
            return Arrays.asList("ping", "-n", "1", ip);
        }
    }

}
