package com.newehcache;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.Eviction;
import org.ehcache.config.ResourcePoolsBuilder;
import org.ehcache.config.SerializerConfiguration;
import org.ehcache.config.serializer.DefaultSerializerConfiguration;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;
import org.ehcache.internal.serialization.CompactJavaSerializer;

import com.newehcache.evictionveto.UserEvictionVeto;
import com.newehcache.model.User;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 16/11/15
 */
public class NewEhcacheDefaultConfigurationDemo {

    public static void main(String[] args) throws Exception {


        // building cache configuration
        CacheConfigurationBuilder<Integer,User> cacheConfigurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder();
        cacheConfigurationBuilder.withExpiry(new Expiry() {
            @Override
            public Duration getExpiryForCreation(Object key, Object value) {
                return new Duration(120, TimeUnit.SECONDS);
            }

            @Override
            public Duration getExpiryForAccess(Object key, Object value) {
                return new Duration(120, TimeUnit.SECONDS);
            }

            @Override
            public Duration getExpiryForUpdate(Object key, Object oldValue, Object newValue) {
                return null;
            }
        })
        .evictionVeto(new UserEvictionVeto())
        .usingEvictionPrioritizer(Eviction.Prioritizer.LFU)
        .withResourcePools(ResourcePoolsBuilder.newResourcePoolsBuilder().heap(200, EntryUnit.ENTRIES))
         // adding defaultSerializer config service to configuration
        .add(new DefaultSerializerConfiguration(CompactJavaSerializer.class, SerializerConfiguration.Type.KEY))
        .buildConfig(Integer.class, User.class);


        // building cache manager
        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("userCache", cacheConfigurationBuilder.buildConfig(Integer.class, User.class))
                .build(false);
        cacheManager.init();

        Cache<Integer, User> preConfigured =
                cacheManager.getCache("userCache", Integer.class, User.class);

        User user1 = new User(1, "user1", "user1", "admin", 100);
        User user2 = new User(2, "user1", "user1", "student", 101);
        preConfigured.put(1, user1);
        preConfigured.put(2, user2);


        // asserting values from cache
        assertEquals(user1,preConfigured.get(1));
        assertEquals(user2,preConfigured.get(2));

        //removing cache from EhcacheManager
        cacheManager.removeCache("preConfigured");

        // Closing cache manager
        cacheManager.close();

    }

}
