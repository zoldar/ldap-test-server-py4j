package net.zoldar.ldap.testserver;

import com.github.trevershick.test.ldap.annotations.LdapAttribute;
import com.github.trevershick.test.ldap.annotations.LdapEntry;
import com.github.trevershick.test.ldap.annotations.Ldif;

import java.lang.annotation.Annotation;

public class LdapBuilder {
    private LdapBuilder() {}

    public static LdapConfigurationBuilder config() {
        return new LdapConfigurationBuilder();
    }

    public static LdapEntry entry(final String dn, final String[] objectclass,
                                  final LdapAttribute[] attributes) {
        return new LdapEntry() {
            @Override
            public String dn() {
                return dn;
            }

            @Override
            public String[] objectclass() {
                return objectclass;
            }

            @Override
            public LdapAttribute[] attributes() {
                return attributes;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static LdapEntry entry(final String dn, final String objectclass,
                                  final LdapAttribute[] attributes) {
        return entry(dn, new String[] {objectclass}, attributes);
    }

    public static LdapAttribute attribute(final String name, final String[] value) {
       return new LdapAttribute() {
           @Override
           public String name() {
               return name;
           }

           @Override
           public String[] value() {
               return value;
           }

           @Override
           public Class<? extends Annotation> annotationType() {
               throw new UnsupportedOperationException();
           }
       };
    }

    public static Ldif ldif(final String value) {
       return new Ldif() {
           @Override
           public String value() {
               return value;
           }

           @Override
           public Class<? extends Annotation> annotationType() {
               throw new UnsupportedOperationException();
           }
       };
    }
}
