package io.github.invvk.seniorparkour.database.manager;

import io.github.invvk.seniorparkour.database.user.User;

import java.util.List;
import java.util.UUID;

public interface IDataManager {

    User getUserFromDatabase(UUID uuid, String name);

    void saveUser(User user);

    List<TopPlayerDOA> getTopPlayers(String gameId);
}
