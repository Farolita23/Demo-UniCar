package com.daw.security;

import java.time.Duration;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

/**
 * Carga, para cada peticion con JWT, los datos minimos del usuario necesarios para autenticarla
 * (nombre y rol; nunca la contrasena) sin leer el resto de columnas de la fila.
 *
 * El resultado se guarda en una cache en memoria de vida corta. Para que los cambios de un usuario
 * (rol, contrasena, baneo, strikes, borrado...) surtan efecto de inmediato, se registra un listener de
 * Hibernate que expulsa de la cache a ese usuario en cuanto se confirma la transaccion que lo modifica.
 * La caducidad de 30 s solo es una red de seguridad (p. ej. cambios hechos directamente en la base de datos).
 *
 * El inicio de sesion NO usa esta cache: sigue pasando por {@link UserDetailsServiceImpl}.
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedUserCache {

    private static final String QUERY =
            "select u.username, u.role, u.banned from User u where u.username = :username";

    /** Marcador: UserDetails exige una contrasena, pero el filtro JWT no la utiliza. */
    private static final String NO_PASSWORD = "[no-disponible]";

    private final EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    private final Cache<String, UserDetails> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofSeconds(30))
            .build();

    /**
     * @param username nombre de usuario extraido del token
     * @return datos de autenticacion del usuario
     * @throws UsernameNotFoundException si el usuario ya no existe (igual que {@link UserDetailsServiceImpl})
     */
    public UserDetails load(String username) {
        UserDetails cached = cache.getIfPresent(username);
        if (cached != null) {
            return cached;
        }
        var rows = entityManager.createQuery(QUERY, Object[].class)
                .setParameter("username", username)
                .getResultList();
        if (rows.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        Object[] row = rows.get(0);
        boolean banned = Boolean.TRUE.equals(row[2]);
        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername((String) row[0])
                .password(NO_PASSWORD)   // el filtro no usa credenciales: el hash no se guarda en memoria
                .roles((String) row[1])
                .disabled(banned)        // un usuario baneado queda deshabilitado aunque su token siga sin caducar
                .build();
        cache.put(username, details);
        return details;
    }

    public void evict(String username) {
        if (username != null) {
            cache.invalidate(username);
        }
    }

    @PostConstruct
    void registerListeners() {
        SessionFactoryImplementor sf = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        EventListenerRegistry registry = sf.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.appendListeners(EventType.POST_COMMIT_UPDATE, new UpdateListener());
        registry.appendListeners(EventType.POST_COMMIT_DELETE, new DeleteListener());
    }

    private static boolean isUser(EntityPersister persister) {
        return "com.daw.datamodel.entities.User".equals(persister.getEntityName());
    }

    private class UpdateListener implements PostCommitUpdateEventListener {
        @Override
        public void onPostUpdate(PostUpdateEvent event) {
            if (!isUser(event.getPersister())) {
                return;
            }
            String[] names = event.getPersister().getPropertyNames();
            for (int i = 0; i < names.length; i++) {
                if ("username".equals(names[i])) {
                    if (event.getOldState() != null) {
                        evict((String) event.getOldState()[i]);
                    }
                    evict((String) event.getState()[i]);
                }
            }
        }

        @Override
        public void onPostUpdateCommitFailed(PostUpdateEvent event) {
            // no hay nada que expulsar: la transaccion no se confirmo
        }

        @Override
        public boolean requiresPostCommitHandling(EntityPersister persister) {
            return isUser(persister);
        }
    }

    private class DeleteListener implements PostCommitDeleteEventListener {
        @Override
        public void onPostDelete(PostDeleteEvent event) {
            if (!isUser(event.getPersister())) {
                return;
            }
            String[] names = event.getPersister().getPropertyNames();
            for (int i = 0; i < names.length; i++) {
                if ("username".equals(names[i]) && event.getDeletedState() != null) {
                    evict((String) event.getDeletedState()[i]);
                }
            }
        }

        @Override
        public void onPostDeleteCommitFailed(PostDeleteEvent event) {
            // no hay nada que expulsar: la transaccion no se confirmo
        }

        @Override
        public boolean requiresPostCommitHandling(EntityPersister persister) {
            return isUser(persister);
        }
    }
}
