package io.github.invvk.seniorparkour.database.user;

import java.util.Optional;
import java.util.UUID;

public interface IUserManager {

    Optional<User> getUser(UUID uuid);

    User createUser(UUID uuid, String name);

    void invalidate(UUID uuid);

    void invalidateAll();

}
