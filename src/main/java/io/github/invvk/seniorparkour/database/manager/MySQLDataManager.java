package io.github.invvk.seniorparkour.database.manager;

import io.github.invvk.seniorparkour.database.User;
import io.github.invvk.seniorparkour.database.storage.MySQLStorage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@RequiredArgsConstructor
public class MySQLDataManager implements IDataManager {

    private final MySQLStorage storage;

    private static final String PLAYER_SELECT = "";
    private static final String PLAYER_SAVE = "";

    @SneakyThrows
    public User getUserFromDatabase(UUID uuid, String name) {
        final User user = new User(uuid, name);
        // TODO: get from db
        return user;
    }

    @SneakyThrows
    public void saveUser(User user) {
        try (final Connection connection = storage.getConnection();
        PreparedStatement st = connection.prepareStatement(String.format(PLAYER_SAVE, storage.getPdTable()))) {
            // todo: set parameters
            st.executeUpdate();
        }

    }

}
