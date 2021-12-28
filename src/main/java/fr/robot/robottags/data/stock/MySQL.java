package fr.robot.robottags.data.stock;

import fr.robot.robottags.data.AbstractData;
import fr.robot.robottags.manager.ConfigManager;
import fr.robot.robottags.manager.StorageManager;
import fr.robot.robottags.utility.TaskAPI;

import java.sql.*;
import java.util.UUID;

import static fr.robot.robottags.Main.log;

public class MySQL implements AbstractData {

    private Connection connection;

    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void load() {
        try {
            log("&fTrying to connect to the database...");
            connect();
            createTable();
            log("&aSuccessfully connected !");
        } catch (ClassNotFoundException | SQLException e) {
            log("&cAn error occurred while trying to connect to the database.");
            log("&cPlease check your MySQL credentials in the configuration file.");
            log("&cSwitching to YML storage mode by default.");
            StorageManager.setMode(StorageManager.DbMode.YML);
        }
    }

    @Override
    public void close() {
        if(isConnected()) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initPlayer(UUID playerUUID) {
        try {
            if (!hasTag(playerUUID)) {
                PreparedStatement ps2 = getConnection().prepareStatement("INSERT IGNORE INTO robottags_tags"
                        + " (UUID) VALUES (?)");
                ps2.setString(1, playerUUID.toString());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasTag(UUID playerUUID) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM  robottags_tags WHERE UUID=?");
            ps.setString(1, playerUUID.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void setTagId(UUID playerUUID, String tag) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE robottags_tags SET TAG = ? WHERE UUID = ?");
            ps.setString(1, tag);
            ps.setString(2, playerUUID.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTagId(UUID playerUUID) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT TAG FROM robottags_tags WHERE UUID=?");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getString("TAG");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            String host = ConfigManager.getConfig().get().getString("storage.mysql-credentials.host");
            String port = ConfigManager.getConfig().get().getString("storage.mysql-credentials.port");
            String database = ConfigManager.getConfig().get().getString("storage.mysql-credentials.database");
            String username = ConfigManager.getConfig().get().getString("storage.mysql-credentials.username");
            String password = ConfigManager.getConfig().get().getString("storage.mysql-credentials.password");

            String ssl = String.valueOf(ConfigManager.getConfig().get().getBoolean("storage.mysql-credentials.useSSL"));
            String autoReconnectStr = ConfigManager.getConfig().get().getString("storage.mysql.auto-reconnect");
            boolean autoReconnect = autoReconnectStr == null || Boolean.parseBoolean(autoReconnectStr);

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?useSSL=" + ssl +
                    "?autoReconnect=" + autoReconnect,
                    username, password);
        }
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS robottags_tags (UUID VARCHAR(100),TAG VARCHAR(100),PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void autoReconnect() {
        Long delay = 20 * 60 * 30L; //30 minutes
        TaskAPI.runTaskTimer(() -> {

            try {
                disconnect();
                connect();
            } catch (ClassNotFoundException | SQLException e) {
                log("&cAn error occurred during the auto-reconnection:" + e.getMessage());
            }

        }, delay, delay);
    }
}
