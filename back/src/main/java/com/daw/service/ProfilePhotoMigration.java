package com.daw.service;

import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.daw.datamodel.entities.User;
import com.daw.datamodel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Al arrancar, pasa a archivos las fotos que aún estén guardadas en la base de datos como
 * {@code data:image/...;base64,...}. Si no queda ninguna, no hace nada.
 */
@Component
@RequiredArgsConstructor
public class ProfilePhotoMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProfilePhotoMigration.class);

    private final UserRepository userRepository;
    private final ProfilePhotoStorage photoStorage;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<User> users = userRepository.findByProfileImageUrlStartingWith("data:");
        for (User user : users) {
            try {
                String data = user.getProfileImageUrl();
                byte[] bytes = Base64.getDecoder().decode(data.substring(data.indexOf(',') + 1));
                user.setProfileImageUrl(photoStorage.store(user.getId(), bytes));
                userRepository.save(user);
            } catch (RuntimeException e) {
                // Foto corrupta: se quita para que no siga viajando en cada respuesta
                log.warn("No se pudo migrar la foto del usuario {}: {}", user.getId(), e.getMessage());
                user.setProfileImageUrl(null);
                userRepository.save(user);
            }
        }
        if (!users.isEmpty()) {
            log.info("Fotos de perfil migradas de la base de datos a archivos: {}", users.size());
        }
    }
}
