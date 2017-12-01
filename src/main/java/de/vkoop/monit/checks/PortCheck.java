package de.vkoop.monit.checks;

import lombok.RequiredArgsConstructor;

import java.net.Socket;

@RequiredArgsConstructor
public class PortCheck extends NamedHealthCheck {

    public final int port;
    public final String host;
    public final String name;

    @Override
    protected Result check() {
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
