package ies.sunday_crypto.Configs;

import static java.util.Arrays.asList;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    ConcurrentMapCacheManager mgr = new ConcurrentMapCacheManager();
    mgr.setCacheNames(asList("coin"));
    return mgr;
  }
}