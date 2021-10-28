package fr.robot.robottags.manager;

import java.sql.*;
import java.util.UUID;

public class MysqlManager {

    private static Connection connection;

    public static boolean isConnected() {
        return connection != null;
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        if(!isConnected()) {
            String host = ConfigManager.getConfig().get().getString("storage.mysql-credentials.host");
            String port = ConfigManager.getConfig().get().getString("storage.mysql-credentials.port");
            String database = ConfigManager.getConfig().get().getString("storage.mysql-credentials.database");
            String username = ConfigManager.getConfig().get().getString("storage.mysql-credentials.username");
            String password = ConfigManager.getConfig().get().getString("storage.mysql-credentials.password");

            String ssl = String.valueOf(ConfigManager.getConfig().get().getBoolean("storage.mysql-credentials.useSSL"));

            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + ssl, username, password);
        }
    }

    public static void disconnect() {
        if(!isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void createTable() {
        PreparedStatement ps;
        try {
            ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS robottags_tags (UUID VARCHAR(100),TAG VARCHAR(100),PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPlayer(UUID uuid) {
        try {
            if (!exists(uuid)) {
                PreparedStatement ps2 = getConnection().prepareStatement("INSERT IGNORE INTO robottags_tags"
                        + " (UUID) VALUES (?)");
                ps2.setString(1, uuid.toString());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM  robottags_tags WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setTag(UUID uuid, String tagID) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("UPDATE robottags_tags SET TAG = ? WHERE UUID = ?");
            ps.setString(1, tagID);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getTag(UUID uuid) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT TAG FROM robottags_tags WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return rs.getString("TAG");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
