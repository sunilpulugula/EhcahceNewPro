package com.newehcache.listener;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import com.newehcache.model.User;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 22/11/15
 */
public class UserListener implements CacheEventListener {

    @Override
    public void onEvent(CacheEvent event) {
        Integer id = (Integer)event.getKey();
        User user = (User)event.getNewValue();
        //do some work when event is triggered.
        if(user.getTenantid() > 50)
        {
            System.out.println("Listener invoked when adding user" + user.getUserid());
        }
    }
}
