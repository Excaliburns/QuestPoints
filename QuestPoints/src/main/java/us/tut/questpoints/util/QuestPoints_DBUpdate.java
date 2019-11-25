package us.tut.questpoints.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.scheduler.BukkitRunnable;
import us.tut.questpoints.QuestPoints;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class QuestPoints_DBUpdate extends BukkitRunnable {
    private QuestPoints questPoints;
    private FileConfiguration fileConfiguration;
    private HashMap<String, Integer> cache;

    public QuestPoints_DBUpdate(QuestPoints questPoints){
        this.questPoints = questPoints;
        this.fileConfiguration = QuestPoints.getConfigurationOptions();
        this.cache = QuestPoints.getLocalPoints();
    }

    public void run() {
        Logger log = Logger.getLogger("Minecraft");
        Statement statement = null;

        try {
            statement = QuestPoints_Database.dbConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(statement == null) {
            log.severe("Could not get database connection on db update. Is your database running?!");
            return;
        }

        System.out.println(QuestPoints.isNeedsUpdate());

        if(!QuestPoints.isNeedsUpdate()) {
            log.info("No update required.");
            return;
        }

        log.info("Updating QuestPoints DB");
        synchronized (this) {

            String tableName = fileConfiguration.getString("qp-table");
            for(Map.Entry<String, Integer> entry : cache.entrySet()){
                String SQLStatement = "INSERT INTO " + tableName + " (uuid, points) VALUES('" + entry.getKey() + "' , " + entry.getValue() + ") ON DUPLICATE KEY UPDATE points=" + entry.getValue() + ";";

                try {
                    statement.executeUpdate(SQLStatement);
                } catch (SQLException e) {
                    log.severe("Could not update table! Exception: " + e);
                    e.printStackTrace();
                }

                QuestPoints.setNeedsUpdate(false);
            }
        }
    }
}
