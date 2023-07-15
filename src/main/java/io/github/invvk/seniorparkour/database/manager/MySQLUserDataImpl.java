package io.github.invvk.seniorparkour.database.manager;

import io.github.invvk.seniorparkour.database.user.User;
import io.github.invvk.seniorparkour.database.user.UserParkourGames;
import io.github.invvk.seniorparkour.database.storage.MySQLStorage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MySQLUserDataImpl implements IDataManager {

    private static final String PLAYER_SELECT = "SELECT game_id, MIN(time) AS 'min' FROM %s where uuid=? GROUP BY game_id;";
    private static final String PLAYER_SAVE = "UPDATE %s SET time=? WHERE uuid=?;";
    private static final String SELECT_TOP = "SELECT DISTINCT uuid, MIN(time) AS 'min' FROM %s where game_id=? GROUP BY uuid ORDER BY min ASC LIMIT 3;";
    private final MySQLStorage storage;

    @SneakyThrows
    public User getUserFromDatabase(UUID uuid, String name) {
        final User user = new User(uuid, name);
        var games = user.getParkours();
        try (var con = storage.getConnection();
             var st = con.prepareStatement(String.format(PLAYER_SELECT, storage.getPdTable()));) {
            st.setString(1, uuid.toString());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    String gameName = rs.getString("game_id");
                    games.put(gameName, UserParkourGames.of(gameName, rs.getLong("min"),
                            false));
                }
            }
        }

        return user;
    }

    @SneakyThrows
    public void saveUser(User user) {
        try (var connection = storage.getConnection();
             var st = connection.prepareStatement(String.format(PLAYER_SAVE, storage.getPdTable()))) {
            for (var entry : user.getParkours().entrySet()) {
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

    @SneakyThrows
    @Override
    public List<TopPlayerDOA> getTopPlayers(String gameId) {
        List<TopPlayerDOA> players = new ArrayList<>();
        try (var con = storage.getConnection();
             var st = con.prepareStatement(String.format(SELECT_TOP, storage.getPdTable()))) {
            st.setString(1, gameId);
            try (var rs = st.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    var uuid = UUID.fromString(rs.getString("uuid"));
                    players.add(TopPlayerDOA.of(Bukkit.getOfflinePlayer(uuid).getName(), rs.getLong("min"), i++));
                }
            }
        }
        return players;
    }

}