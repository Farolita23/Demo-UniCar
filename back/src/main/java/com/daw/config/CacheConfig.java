package com.daw.config;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * Cache en memoria de los catalogos que casi nunca cambian (campus y localidades).
 *
 * Las entradas se invalidan al crear, modificar o borrar (con efecto tras el commit de la
 * transaccion, para no volver a cachear datos antiguos) y caducan solas pasados 2 minutos.
 * Ademas, los GET publicos de listado llevan ETag para que el navegador pueda revalidar con 304.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CAMPUS = "campus";
    public static final String TOWN = "town";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(CAMPUS, TOWN);
        manager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(2)));
        return new TransactionAwareCacheManagerProxy(manager);
    }

    /** ETag calculado sobre la respuesta de los listados publicos de catalogos. */
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> catalogEtagFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> reg = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        reg.setUrlPatterns(List.of("/api/campus", "/api/town"));
        reg.setName("catalogEtagFilter");
        return reg;
    }
}
