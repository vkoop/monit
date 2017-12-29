package de.vkoop.monit.checks.impl;

import de.vkoop.monit.checks.NamedHealthCheck;
import de.vkoop.monit.checks.Result;
import lombok.RequiredArgsConstructor;

import java.net.Socket;

/**
 * Health check to check if port is open and reachable.
 */
@RequiredArgsConstructor
public class PortCheck implements NamedHealthCheck {

    public final int port;
    public final String host;
    public final String name;

    @Override
    public Result check() {
        try (Socket socket = new Socket(host, port);
        ) {
            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
