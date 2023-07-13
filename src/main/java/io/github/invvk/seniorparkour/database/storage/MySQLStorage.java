package io.github.invvk.seniorparkour.database.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.invvk.seniorparkour.SeniorParkour;
import io.github.invvk.seniorparkour.config.holder.ConfigProperties;
import io.github.invvk.seniorparkour.database.manager.IDataManager;
import io.github.invvk.seniorparkour.database.manager.MySQLDataManager;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class MySQLStorage implements IStorage {


    @Getter private String tablePrefix;

    @Getter private String pdTable;

    private HikariDataSource dataSource;

    private MySQLDataManager dataManager;

    @Override
    public void init() {
        var cnf = SeniorParkour.inst().getConfig();
        this.tablePrefix = cnf.getString(ConfigProperties.STORAGE_TABLE_PREFIX);
        this.pdTable = tablePrefix + "_player_data";

        HikariConfig config = new HikariConfig();

        config.setPoolName("SeniorParkour-Pool");
        config.setJdbcUrl("jdbc:mysql://" + cnf.getString(ConfigProperties.STORAGE_HOST) + ":"
                + cnf.getString(ConfigProperties.STORAGE_PORT) + "/" + cnf.getString(ConfigProperties.STORAGE_DATABASE));
        config.setUsername(cnf.getString(ConfigProperties.STORAGE_USER));
        config.setPassword(cnf.getString(ConfigProperties.STORAGE_PASSWORD));
        config.setIdleTimeout(10000);
        config.setMaxLifetime(60000);
        config.setMinimumIdle(1);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        final Properties properties = new Properties();
        properties.putIfAbsent("serverTimezone", "UTC");

        config.setDataSourceProperties(properties);

        dataSource = new HikariDataSource(config);
        this.initTable();

        this.dataManager = new MySQLDataManager(this);
    }

    private void initTable() {
        if (dataSource == null)
            return;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement(
                     String.format("CREATE TABLE IF NOT EXISTS %s(uuid VARCHAR(36) NOT NULL, name VARCHAR(16) NOT NULL, cooldown BIGINT, amountLeft DOUBLE, PRIMARY KEY (uuid, name), UNIQUE (uuid), UNIQUE(name))", this.pdTable))) {
            st.executeUpdate();
        } catch (SQLException e) {
            SeniorParkour.inst().getLogger().log(Level.SEVERE, "Failed to create table", e);
        }
    }

    @Override
    public void close() {
        if (this.dataSource != null)
            this.dataSource.close();
    }

    @Override
    public IDataManager getDataManager() {
        return this.dataManager;
    }

    public final Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

}
