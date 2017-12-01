package de.vkoop.monit.checks;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class PingCheck extends NamedHealthCheck {

    @NonNull
    private final String ip;
    @NonNull
    private final PingCommandStrategy pingCommandStrategy;
    @NonNull
    private final String name;

    @Override
    protected Result check() {
        List<String> command = pingCommandStrategy.getCommandForIp(ip);

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
            return Result.unhealthy(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public interface PingCommandStrategy {
        List<String> getCommandForIp(String ip);
    }

    @Profile("linux")
    @Component
    public static class LinuxPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getCommandForIp(String ip) {
            return Arrays.asList("ping", "-c1", ip);
        }
    }

    @Profile("windows")
    @Component
    public static class WindowsPingStrategy implements PingCommandStrategy {
        @Override
        public List<String> getCommandForIp(String ip) {
            return Arrays.asList("ping", "-n", "1", ip);
        }
    }

}
