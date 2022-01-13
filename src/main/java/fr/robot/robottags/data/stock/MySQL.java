package fr.robot.robottags.data.stock;

import com.zaxxer.hikari.HikariDataSource;
import fr.robot.robottags.data.AbstractData;
import fr.robot.robottags.manager.ConfigManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static fr.robot.robottags.Main.log;

public class MySQL implements AbstractData {

    HikariDataSource dataSource;

    @Override
    public void load() {
        connect();
        createTable();
        log("&aSuccessfully connected to database.");
    }

    public void connect() {
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(5);
        dataSource.setJdbcUrl("jdbc:mysql://" +
                ConfigManager.getConfig().get().getString("storage.mysql-credentials.host") + ":" +
                ConfigManager.getConfig().get().getString("storage.mysql-credentials.port") + "/" +
                ConfigManager.getConfig().get().getString("storage.mysql-credentials.database"));
        dataSource.addDataSourceProperty("user", ConfigManager.getConfig().get().getString("storage.mysql-credentials.username"));
        dataSource.addDataSourceProperty("password", ConfigManager.getConfig().get().getString("storage.mysql-credentials.password"));
        dataSource.addDataSourceProperty("useSSL", ConfigManager.getConfig().get().getBoolean("storage.mysql-credentials.useSSL"));
    }

    public void createTable() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS robottags_tags (UUID VARCHAR(100),TAG VARCHAR(100),PRIMARY KEY (UUID))")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initPlayer(UUID playerUUID) {
        if (!hasTag(playerUUID)) {
            try (Connection connection = dataSource.getConnection(); PreparedStatement ps = connection.prepareStatement("INSERT IGNORE INTO robottags_tags"
                    + " (UUID) VALUES (?)")) {
                ps.setString(1, playerUUID.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getTagId(UUID playerUUID) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT TAG FROM robottags_tags WHERE UUID=?")) {
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TAG");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setTagId(UUID playerUUID, String tag) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE robottags_tags SET TAG = ? WHERE UUID = ?")) {
            ps.setString(1, tag);
            ps.setString(2, playerUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasTag(UUID playerUUID) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM  robottags_tags WHERE UUID=?")) {
            ps.setString(1, playerUUID.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
