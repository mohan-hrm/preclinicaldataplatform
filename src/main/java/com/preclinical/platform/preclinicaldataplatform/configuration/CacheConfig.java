package com.preclinical.platform.preclinicaldataplatform.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
            "studies", 
            "patients", 
            "studyStatistics", 
            "safetyReports",
            "efficacyAnalysis"
        );
        return cacheManager;
    }
    
    @Bean
    @Profile("prod")
    public CacheManager redisCacheManager() {
        // Redis cache configuration for production
        // Implementation would configure Redis connection
        return new ConcurrentMapCacheManager(); // Placeholder
    }
}
