package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import py4j.GatewayServer;

import java.util.HashMap;

public class Server {
    private static GatewayServer gateway;

    private HashMap<Integer, LdapServerResource> servers;

    public static void main(String[] args) throws Exception {
        gateway = new GatewayServer(new Server());
        gateway.start();
        System.out.println("Gateway server started on port "+gateway.getPort()+"!");
    }

    private Server() {
        servers = new HashMap<Integer, LdapServerResource>();
    }

    public int create(LdapConfiguration config) throws Exception {
        LdapServerResource new_server;
        int port;

        port = config.port();

        if (servers.containsKey(port)) {
            if (servers.get(port).isStarted()) {
                throw new IllegalStateException("LDAP server already running on port " + port);
            }
        }

        new_server = new LdapServerResourceCreator(config).getResource();
        servers.put(port, new_server);
        return port;
    }

    public void destroy(int serverId) {
        if (servers.containsKey(serverId)) {
            servers.get(serverId).stop();
        }

        servers.remove(serverId);
    }

    public void start() throws Exception {
        for (Integer key : servers.keySet()) {
            if (!servers.get(key).isStarted()) {
                servers.get(key).start();
            }
        }
    }

    public void start(int serverId) throws Exception {
        if (!servers.get(serverId).isStarted()) {
            servers.get(serverId).start();
        }
    }

    public void stop() {
        for (Integer key : servers.keySet()) {
            servers.get(key).stop();
        }
    }

    public void stop(int serverId) {
        if (servers.containsKey(serverId)) {
            servers.get(serverId).stop();
        }
    }

    /* functions provided for backwards compatability */
    public void start(LdapConfiguration config) throws Exception {
        LdapServerResource new_server;
        new_server = new LdapServerResourceCreator(config).getResource();
        new_server.start();

        // stop any existing servers running and clean them up
        stop();
        for (Integer key : servers.keySet()) {
            servers.remove(key);
        }

        servers.put(new_server.port(), new_server);
    }

    public int port() {
        return servers.values().toArray(new LdapServerResource[0])[0].port();
    }
}
