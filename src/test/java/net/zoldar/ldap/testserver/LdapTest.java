package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.annotations.LdapEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
@LdapConfiguration(
	bindDn = "cn=Directory Manager", 
	password = "mypass", 
	port = 11111, 
	base = @LdapEntry(dn = "dc=myroot", objectclass = { "top", "domain" }))
public class LdapTest {
    private LdapServerResource server;

    @Before
    public void startup() throws Exception {
	    server = new LdapServerResource(this).start();
    }

    @After
    public void shutdown() {
    	server.stop();
    }

    @Test
    public void basicServerTest() {
        assertTrue(server.isStarted());
    }
}
