package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.annotations.LdapAttribute;
import com.github.trevershick.test.ldap.annotations.LdapConfiguration;
import com.github.trevershick.test.ldap.annotations.LdapEntry;
import com.github.trevershick.test.ldap.annotations.Ldif;

import java.lang.annotation.Annotation;

public class LdapConfigurationBuilder {
    private boolean useRandomPortAsFallback = false;
    private int port = LdapConfiguration.DEFAULT_PORT;
    private LdapEntry base = LdapBuilder.entry(
            LdapConfiguration.DEFAULT_ROOT_OBJECT_DN,
            LdapConfiguration.DEFAULT_ROOT_ENTRY_OBJECTCLASS,
            new LdapAttribute[0]);
    private LdapEntry[] entries;
    private Ldif[] ldifs;
    private String bindDn;
    private String password;

    public LdapConfigurationBuilder() {

    }

    public LdapConfigurationBuilder useRandomPortAsFallback(boolean flag) {
        useRandomPortAsFallback = flag;
        return this;
    }

    public LdapConfigurationBuilder port(int port) {
        this.port = port;
        return this;
    }

    public LdapConfigurationBuilder base(LdapEntry base) {
        this.base = base;
        return this;
    }

    public LdapConfigurationBuilder entries(LdapEntry[] entries) {
        this.entries = entries;
        return this;
    }

    public LdapConfigurationBuilder ldifs(Ldif[] ldifs) {
        this.ldifs = ldifs;
        return this;
    }

    public LdapConfigurationBuilder bindDn(String bindDn) {
        this.bindDn = bindDn;
        return this;
    }

    public LdapConfigurationBuilder password(String password) {
        this.password = password;
        return this;
    }

    public LdapConfiguration build() {
        return new LdapConfiguration() {
            @Override
            public boolean useRandomPortAsFallback() {
                return useRandomPortAsFallback;
            }

            @Override
            public int port() {
                return port;
            }

            @Override
            public LdapEntry base() {
                return base;
            }

            @Override
            public LdapEntry[] entries() {
                return entries;
            }

            @Override
            public Ldif[] ldifs() {
                return ldifs;
            }

            @Override
            public String bindDn() {
                return bindDn;
            }

            @Override
            public String password() {
                return password;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
