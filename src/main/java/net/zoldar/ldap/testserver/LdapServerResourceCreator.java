package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.LdapServerResource;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;

public class LdapServerResourceCreator extends LdapServerResource {
    private LdapConfiguration config;

    public LdapServerResourceCreator(LdapConfiguration config) {
        this.config = config;
    }

    public LdapServerResource getResource() {
        return new LdapPatchedServerResource() {
            @Override
            protected LdapConfiguration defaultConfiguration() {
                return config;
            }
        };

    }

}
