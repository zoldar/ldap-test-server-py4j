package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapAttribute;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LdapTest {
    private LdapServerResource server;

    @Before
    public void startup() throws Exception {
        LdapConfiguration config = LdapBuilder.config()
                .base(LdapBuilder.entry(
                        "dc=myroot",
                        new String[]{"top", "domain"},
                        new LdapAttribute[]{}))
                .bindDn("cn=Directory Manager")
                .password("mypass")
                .port(11111).build();
        server = new LdapServerResourceCreator(config).getResource();
        server.start();
    }

    @After
    public void shutdown() {
    	server.stop();
    }

    @Test
    public void basicServerTest() {
        assertTrue(server.isStarted());
        assertEquals(server.port(), 11111);
    }
}
