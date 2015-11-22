package com.newehcache.evictionveto;

import org.ehcache.config.EvictionVeto;

import com.newehcache.model.User;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 22/11/15
 */
public class UserEvictionVeto implements EvictionVeto {
    @Override
    public boolean test(Object value) {
        User user = (User) value;
        if(user.getTenantid() > 60)
        {
            return true;
        }
        return false;
    }
}
