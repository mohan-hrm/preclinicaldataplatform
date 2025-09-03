package com.preclinical.platform.preclinicaldataplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableCaching
public class PreclinicaldataplatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(PreclinicaldataplatformApplication.class, args);
    }
}
