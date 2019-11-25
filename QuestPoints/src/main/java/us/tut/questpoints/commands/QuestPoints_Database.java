package us.tut.questpoints.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import us.tut.questpoints.QuestPoints;

import java.sql.*;
import java.util.logging.Logger;

public class QuestPoints_Database {

    private static Connection connection;
    private static FileConfiguration configurationOptions = Bukkit.getPluginManager().getPlugin("QuestPoints").getConfig();

    public static Statement dbConnection() throws SQLException, ClassNotFoundException {

        String host = configurationOptions.getString("qp-host");
        String port = configurationOptions.getString("qp-port");
        String db = configurationOptions.getString("qp-db");
        String username = configurationOptions.getString("qp-user");
        String password = configurationOptions.getString("qp-pass");

        Logger.getLogger("Minecraft").info(host + " " + port + " " + db + " " + username + " " + password);

        if(connection != null && connection.isClosed())
            return null;

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, username, password);

        return connection.createStatement();
    }

}
