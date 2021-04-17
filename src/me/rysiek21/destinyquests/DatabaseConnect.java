package me.rysiek21.destinyquests;

import java.sql.*;

import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseConnect {
	String host, port, database, username, password;
    static Connection connection;
    public void MysqlConnect() {
    	FileConfiguration config = Main.getConfigFile();
    	host = config.getString("mysql.host");
        port = config.getString("mysql.port");
        database = config.getString("mysql.database");
        username = config.getString("mysql.username");
        password = config.getString("mysql.password");
        
        try {
            OpenConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    void OpenConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"
                        + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
        InitialiseDatabase();
    }
    public void InitialiseDatabase() throws SQLException {
    	try {
    		System.out.println("[DestinyQuests] Initialising database");
    		Statement statement = connection.createStatement();
        	statement.executeUpdate("CREATE TABLE IF NOT EXISTS `DestinyQuests` (`UUID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL);");
        	FileConfiguration config = Main.getConfigFile();
        	config.getConfigurationSection("quests").getKeys(false).forEach(quest ->{
        		try {
        			DatabaseMetaData meta = connection.getMetaData();
        			ResultSet result = meta.getColumns(null, null, "DestinyQuests", quest);
        			if (!result.next()) {
        				statement.executeUpdate("ALTER TABLE `DestinyQuests`"
        						+ "ADD COLUMN `" + quest + "` VARCHAR(4) NOT NULL DEFAULT '0';");
        			}
				} catch (SQLException e) {
					e.printStackTrace();
				}
        	});
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
