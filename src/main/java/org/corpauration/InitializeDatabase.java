package org.corpauration;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.vertx.VertxContextSupport;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.corpauration.user.UserEntity;
import org.corpauration.user.UserRepository;

import java.util.Set;

@ApplicationScoped
public class InitializeDatabase {

    @Inject
    UserRepository userRepository;

    public void onStart(@Observes StartupEvent ev) throws Throwable {
        Log.info("Initializing Database...");

        VertxContextSupport.subscribeAndAwait(() -> Panache.withTransaction(() -> {
            UserEntity admin = new UserEntity("admin", "Super", "User", "admin@admin.fr", Set.of("user", "admin"));
            UserEntity user = new UserEntity("user", "John", "Doe", "user@user.fr", Set.of("user"));

            return userRepository.create(admin)
                    .chain(() -> userRepository.create(user));
        }));

        Log.info("Database initialized");
    }
}
