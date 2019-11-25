package us.tut.questpoints;

import org.bukkit.plugin.java.JavaPlugin;
import us.tut.questpoints.commands.BalanceCommand;
import us.tut.questpoints.commands.QuestPoints_Database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class QuestPoints extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Statement statement = null;
    private static HashMap<String, Integer>  localPoints = new HashMap<String, Integer>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        try {
            statement = QuestPoints_Database.dbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        getCommand("qp").setExecutor(new BalanceCommand());

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM points_users");

            while(resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String balance = resultSet.getString("points");
                localPoints.put(uuid, Integer.parseInt(balance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Map.Entry<String, Integer> entry: localPoints.entrySet()) {
            log.info(entry.getKey() + " , " + entry.getValue());
        }
    }

    @Override
    public void onDisable() {
        log.info(String.format("[%s] has been disabled. Version - %s", getDescription().getName(), getDescription().getVersion()));
    }

    public static HashMap<String, Integer> getLocalPoints() {
        return localPoints;
    }
}
