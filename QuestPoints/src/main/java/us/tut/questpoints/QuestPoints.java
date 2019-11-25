package us.tut.questpoints;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import us.tut.questpoints.EventHandlers.Events;
import us.tut.questpoints.commands.BalanceCommand;
import us.tut.questpoints.util.QuestPoints_DBUpdate;
import us.tut.questpoints.util.QuestPoints_Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class QuestPoints extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static Statement statement = null;
    private static HashMap<String, Integer>  localPoints = new HashMap<String, Integer>();
    private static FileConfiguration configurationOptions = null;
    private static boolean needsUpdate = false;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        configurationOptions = Bukkit.getPluginManager().getPlugin("QuestPoints").getConfig();

        long DBUpdateMilli = Integer.parseInt(configurationOptions.getString("update-time")) * 20;

        try {
            statement = QuestPoints_Database.dbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (statement == null){
            log.severe(String.format("[%s] Database connection has failed. Disabling.", getName()));
            getPluginLoader().disablePlugin(this);
        }

        loadDBIntoCache();

        getServer().getPluginManager().registerEvents(new Events(), this);

        BukkitTask dbUpdateEvent = new QuestPoints_DBUpdate(this).runTaskTimerAsynchronously(this, DBUpdateMilli, DBUpdateMilli);

        getCommand("qp").setExecutor(new BalanceCommand());
    }

    @Override
    public void onDisable() {
        log.info(String.format("[%s] has been disabled. Version - %s", getDescription().getName(), getDescription().getVersion()));
    }

    private void loadDBIntoCache()
    {
        configurationOptions = Bukkit.getPluginManager().getPlugin("QuestPoints").getConfig();
        String tableName = configurationOptions.getString("qp-table");

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

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

    public static HashMap<String, Integer> getLocalPoints() {
        return localPoints;
    }

    public static void setLocalPoints(HashMap<String, Integer> localPoints) {
        QuestPoints.localPoints = localPoints;
    }

    public static FileConfiguration getConfigurationOptions() {
        return configurationOptions;
    }

    public static boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public static void setNeedsUpdate(boolean needsUpdate) {
        QuestPoints.needsUpdate = needsUpdate;
    }
}
