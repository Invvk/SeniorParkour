package io.github.invvk.seniorparkour.database;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.database.manager.IDataManager;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.UUID;

public class UserManager implements IUserManager {

    private final Cache<UUID, User> usersCache = CacheBuilder.newBuilder()
            .build();

    private final IDataManager dataManager;

    public UserManager() {
        this.dataManager = SeniorParkour.inst().getStorageManager().getDataManager();
        long updateInterval = SeniorParkour.inst().getConfig().getLong(ConfigProperties.STORAGE_DB_UPDATE_TIMER) * 20 * 60;
        Bukkit.getScheduler().runTaskTimerAsynchronously(SeniorParkour.inst(),
                () -> usersCache.asMap().forEach((uuid, user) -> dataManager.saveUser(user))
                ,updateInterval, updateInterval);
    }

    @Override
    public Optional<User> getUser(UUID uniqueId) {
        return Optional.ofNullable(usersCache.getIfPresent(uniqueId));
    }


    @SneakyThrows
    @Override
    public User createUser(UUID uniqueId, String name) {
        return this.usersCache.get(uniqueId, () -> dataManager.getUserFromDatabase(uniqueId, name));
    }

    @Override
    public void invalidate(UUID uniqueId) {
        final User user = this.usersCache.getIfPresent(uniqueId);
        if (user == null)
            return;

        dataManager.saveUser(user);
        this.usersCache.invalidate(uniqueId);
    }

    @Override
    public void invalidateAll() {
        // Save before Invalidating all user cache
        usersCache.asMap()
                .forEach((uuid, user) -> dataManager.saveUser(user));

        // Invalidate user cache
        this.usersCache.invalidateAll();
    }

}
