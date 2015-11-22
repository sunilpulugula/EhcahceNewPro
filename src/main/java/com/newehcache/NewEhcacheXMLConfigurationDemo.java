package com.newehcache;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.EhcacheManager;
import org.ehcache.config.xml.XmlConfiguration;

import com.newehcache.model.User;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 22/11/15
 */
public class NewEhcacheXMLConfigurationDemo {

    public static int count = 0;

    public static void main(String[] args) {
        try {
            URL url = NewEhcacheDefaultConfigurationDemo.class.getClassLoader().getResource("ehcache/newehcache.xml");
            final CacheManager cacheManager = new EhcacheManager(new XmlConfiguration(url, NewEhcacheDefaultConfigurationDemo.class.getClassLoader()));
            cacheManager.init();
            final Cache<Integer, User> userCache = cacheManager.getCache("userCache", Integer.class, User.class);

            int noOfThreads = 2;
            ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String threadName = "thread_1";

                    //adding users into cache
                    for (int i = 0; i < 3; i++) {
                        int userID = getCount();
                        addUser(userCache, userID);
                        System.out.println("Added user"+ userID + " to userCache using in " + threadName);
                    }

                    int i = 1;
                    while (i <= count) {
                        //any random value between 1 to 60 sec
                        int sleepTime = getRandomSleepTime(1000, 45000);
                        System.out.println(threadName + " will sleep during " + sleepTime + " milliseconds");
                        try {
                            Thread.currentThread().sleep(sleepTime);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                        boolean exist = userCache.containsKey(i);
                        System.out.println("user" + i + (exist ? " exist" : " not exist") + " using " + threadName);
                        i++;
                    }
                }
            });
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String threadName = "thread_2";

                    //adding users into cache
                    for (int i = 0; i < 3; i++) {
                        int userID = getCount();
                        addUser(userCache, userID);
                        System.out.println("Added user"+ userID + " to userCache using in " + threadName);
                    }

                    int i = 1;
                    while (i <= count) {
                        //any random value between 1 to 60 sec
                        int sleepTime = getRandomSleepTime(1000, 60000);
                        System.out.println(threadName + " will sleep during " + sleepTime + " milliseconds");
                        try {
                            Thread.currentThread().sleep(sleepTime);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                        boolean exist = userCache.containsKey(i);
                        System.out.println("user" + i + (exist ? " exist" : " not exist") + " using " + threadName);
                        i++;
                    }
                }
            });

            try {
                //waiting until executor threads are done.
                executorService.shutdown();
                while (!executorService.awaitTermination(24L, TimeUnit.HOURS)) {
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            userCache.clear();
            cacheManager.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



        System.out.println("Bye bye....");
    }

    private static void addUser(Cache<Integer, User> userCache, int id) {
        User user = new User(id, "user" + id, "user" + id, "user", new Random().nextInt(100) + 1);
        System.out.println(user);
        userCache.put(id, user);
    }

    public static int getCount() {
        return ++count;
    }

    private static int getRandomSleepTime(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
