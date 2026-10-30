package com.daw.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.daw.service.ProfilePhotoStorage;

import lombok.RequiredArgsConstructor;

/**
 * Sirve las fotos de perfil guardadas en disco bajo {@code /uploads/**}.
 *
 * Cada subida genera un nombre de archivo nuevo, así que el navegador puede cachearlas un año
 * sin riesgo de mostrar una foto antigua.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ProfilePhotoStorage photoStorage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(photoStorage.getRoot().toUri().toString())
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic().immutable());
    }
}
