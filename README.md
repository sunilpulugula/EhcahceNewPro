**Ehcahce demo with new API(version 3.0)**


It’s been 5 years since Ehcache released Ehcache 2.0.Now Ehcache 3.0 is one of the most feature rich caching APIs out there.In the meantime while some caching solutions took very different approaches on it all, the expert group on JSR-107, Terracotta included, put great efforts in trying to come up with a standard API in 3.0.Below are the features introduced in Ehcache 3.0

1. Ehcache 3.0 will likely “extend” the specification of JSR-107.
2. Ehcache 2.0 constrain caches on-heap or on-disk.3.0 introduced more tiers to cache data in (off-heap, Terracotta clustered).
3. 3.0 new API is ready for the immediate future that is Java 8.
4. Ehcache 3.0 API supporting Lamdas.
5. Introduced BootstrapCacheLoader (sync/async),ehanced Statistics,Search & Transaction.
6. Introduced Write Through,Write Behind,Read Through & Refresh Ahead Features.
7. Many more......

This project will demonstrate about building cache using Ehcache 3.0.That is Ehcahce can build with two types of configurations
* XMLConfiguration.
* Building configuration using API.
