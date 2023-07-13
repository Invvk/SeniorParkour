package io.github.invvk.seniorparkour.database.manager;

import io.github.invvk.seniorparkour.database.user.User;
import io.github.invvk.seniorparkour.database.user.UserParkourGames;
import io.github.invvk.seniorparkour.database.storage.MySQLStorage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.UUID;

@RequiredArgsConstructor
public class MySQLUserDataImpl implements IDataManager {

    private final MySQLStorage storage;

    private static final String PLAYER_SELECT = "SELECT game_id, MIN(time) AS 'min' FROM %s GROUP BY game_id;";
    private static final String PLAYER_SAVE = "UPDATE %s SET time=? WHERE uuid=?;";

    @SneakyThrows
    public User getUserFromDatabase(UUID uuid, String name) {
        final User user = new User(uuid, name);
        var games = user.getParkours();
        try (var con = storage.getConnection();
             var st = con.prepareStatement(String.format(PLAYER_SELECT, storage.getPdTable()));
             var rs = st.executeQuery()) {
            while (rs.next()) {
                String gameName = rs.getString("game_id");
                games.put(gameName, UserParkourGames.of(gameName, rs.getLong("min"),
                        false));
            }
        }

        return user;
    }

    @SneakyThrows
    public void saveUser(User user) {
        try (var connection = storage.getConnection();
             var st = connection.prepareStatement(String.format(PLAYER_SAVE, storage.getPdTable()))) {
            for (var entry: user.getParkours().entrySet()) {
                var data = entry.getValue();
                if (data.isModified()) {
                    st.setLong(1, data.getTime());
                    st.setString(2, user.getUniqueId().toString());
                    st.addBatch();
                }
            }
            st.executeBatch();
        }

    }

}
