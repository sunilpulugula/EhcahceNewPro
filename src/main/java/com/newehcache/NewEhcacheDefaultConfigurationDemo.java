package com.newehcache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.config.CacheConfigurationBuilder;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expiry;

import com.newehcache.model.User;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 16/11/15
 */
public class NewEhcacheDefaultConfigurationDemo {

    public static void main(String[] args) throws Exception{


        CacheConfigurationBuilder cacheConfigurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder();
        cacheConfigurationBuilder.withExpiry(new Expiry() {
            @Override
            public Duration getExpiryForCreation(Object key, Object value) {
                return new Duration(300, TimeUnit.SECONDS);
            }

            @Override
            public Duration getExpiryForAccess(Object key, Object value) {
                return null;
            }

            @Override
            public Duration getExpiryForUpdate(Object key, Object oldValue, Object newValue) {
                return null;
            }
        });
        cacheConfigurationBuilder.withResourcePools(new ResourcePools() {
            @Override
            public ResourcePool getPoolForResource(ResourceType resourceType) {
                return null;
            }

            @Override
            public Set<ResourceType> getResourceTypeSet() {
                return null;
            }
        });

        CacheManager cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder()
                                .buildConfig(Integer.class, User.class))
                .build(false);
        cacheManager.init();

        Cache<Integer, User> preConfigured =
                cacheManager.getCache("preConfigured", Integer.class, User.class);

        preConfigured.put(1,new User(1,"user1","user1","admin",100));
        preConfigured.put(2,new User(2,"user1","user1","student",101));
        preConfigured.put(3,new User(3,"user1","user1","student",102));


        System.out.println(preConfigured.get(1).getUserid());

        cacheManager.removeCache("preConfigured");

        cacheManager.close();

/*        URL url = SimpleEhcacheDemo.class.getClassLoader().getResource("ehcache/userCache.xml");
        CacheManager cacheManager1 = new EhcacheManager(new XmlConfiguration(url,SimpleEhcacheDemo.class.getClassLoader()));
        cacheManager1.init();
        Cache<Integer, User> cache1 = cacheManager1.getCache("userCache2", Integer.class, User.class);
        cache1.put(1,new User(1,"user1","user1","admin",30));
        cache1.put(2,new User(2,"user1","user1","student",101));
        cache1.put(3,new User(3,"user1","user1","student",102));


        cache1.remove(1);
        cache1.containsKey(1);
        cache1.get(2);
        cache1.clear();*/
    }

}
