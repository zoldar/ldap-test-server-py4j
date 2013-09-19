package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.BindException;

public class LdapPatchedServerResource extends LdapServerResource {
    private InMemoryDirectoryServer server;

    protected InMemoryDirectoryServer configureWithPort(int port) throws LDAPException,BindException {
        this.server = super.configureWithPort(port);
        return server;
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
