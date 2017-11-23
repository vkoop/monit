package de.vkoop.monit.checks;

import java.net.Socket;

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

    public PortCheck(int port, String host, String name) {
        this.port = port;
        this.host = host;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
