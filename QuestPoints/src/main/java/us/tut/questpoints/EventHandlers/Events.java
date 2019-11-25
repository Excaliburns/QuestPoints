package us.tut.questpoints.EventHandlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import us.tut.questpoints.QuestPoints;
import us.tut.questpoints.util.QuestPoints_DBUpdate;

import java.util.HashMap;

public class Events implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerUUID = event.getPlayer().getUniqueId().toString().replaceAll("-", "");
        HashMap<String, Integer> cache = QuestPoints.getLocalPoints();

        if(!cache.containsKey(playerUUID)){
            cache.put(playerUUID, 0);
        }

        QuestPoints.setLocalPoints(cache);
    }

    @EventHandler
    public void beforeDisable(PluginDisableEvent event){
        System.out.println("Saving DB before disable");
        if(event.getPlugin() instanceof QuestPoints)
            new QuestPoints_DBUpdate((QuestPoints) event.getPlugin()).run();
    }
}
