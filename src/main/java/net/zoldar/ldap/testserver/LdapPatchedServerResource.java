package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.BindException;

public class LdapPatchedServerResource extends LdapServerResource {
    private InMemoryDirectoryServer server;

    protected InMemoryDirectoryServer configureWithPort(int port) throws LDAPException,BindException {
        LdapConfiguration config = defaultConfiguration();

        InMemoryListenerConfig listenerConfig = (port > 0) ?
                InMemoryListenerConfig.createLDAPConfig("default", config.port()) :
                InMemoryListenerConfig.createLDAPConfig("default");

        InMemoryDirectoryServerConfig c = new InMemoryDirectoryServerConfig(new DN(config.base().dn()));
        c.setListenerConfigs(listenerConfig);
        c.addAdditionalBindCredentials(config.bindDn(), config.password());
        c.setSchema(null);

        server = new InMemoryDirectoryServer(c);
        try {
            server.startListening();
            return server;
        } catch (LDAPException ldapException) {
            if (ldapException.getMessage().contains("java.net.BindException")) {
                throw new BindException(ldapException.getMessage());
            }
            throw ldapException;
        }
    }

    protected void loadLdifFiles() throws Exception {
        Iterable<String> ldifResources = ldifResources();
        for (String ldif : ldifResources) {
            InputStream ldifStream = new FileInputStream(ldif);
            try {
                LDIFReader r = new LDIFReader(ldifStream);
                LDIFChangeRecord readEntry;
                while ((readEntry = r.readChangeRecord()) != null) {
                    readEntry.processChange(server);
                }
            } finally {
                ldifStream.close();
            }
        }
    }
}
