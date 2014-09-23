package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import py4j.GatewayServer;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private static GatewayServer gateway;

    private Map<Integer, LdapServerResource> servers = new HashMap<Integer, LdapServerResource>();

    public static void main(String[] args) throws Exception {
        gateway = new GatewayServer(new Server());
        gateway.start();
        System.out.println("Gateway server started on port "+gateway.getPort()+"!");
    }

    private Server() {}

    public int create(LdapConfiguration config) throws Exception {
        int port = config.port();

        if (servers.containsKey(port)) {
            if (servers.get(port).isStarted()) {
                throw new IllegalStateException("LDAP server already running on port " + port);
            }
        }

        LdapServerResource newServer = new LdapServerResourceCreator(config).getResource();
        servers.put(port, newServer);
        return port;
    }

    public void destroy() {
        for (int serverId : servers.keySet()) {
            destroy(serverId);
        }
    }

    public void destroy(int serverId) {
        if (servers.containsKey(serverId)) {
            servers.get(serverId).stop();
        }

        servers.remove(serverId);
    }

    public void start() throws Exception {
        for (int serverId : servers.keySet()) {
            if (!servers.get(serverId).isStarted()) {
                servers.get(serverId).start();
            }
        }
    }

    public void start(int serverId) throws Exception {
        if (!servers.get(serverId).isStarted()) {
            servers.get(serverId).start();
        }
    }

    public void stop() {
        for (int serverId : servers.keySet()) {
            servers.get(serverId).stop();
        }
    }

    public void stop(int serverId) {
        if (servers.containsKey(serverId)) {
            servers.get(serverId).stop();
        }
    }

    /* functions provided for backwards compatability */
    public void start(LdapConfiguration config) throws Exception {
        LdapServerResource newServer = new LdapServerResourceCreator(config).getResource();
        newServer.start();

        // destroy any existing servers running
        destroy();

        servers.put(newServer.port(), newServer);
    }

    public int port() {
        return servers.values().iterator().next().port();
    }
}
