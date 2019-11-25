package us.tut.questpoints.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.tut.questpoints.QuestPoints;

import java.util.HashMap;

class UpdatePoints {
    UpdatePoints(CommandSender commandSender, Command command, String[] args)
    {
        updateMoney(commandSender, command, args);
    }

    private void updateMoney(CommandSender commandSender, Command command, String[] args) {
        try{
            int pointsToUpdate = Integer.parseInt(args[1]);
            Player player;
            if(args[0].toLowerCase().equals("remove")){
                pointsToUpdate *= -1;
            }

            HashMap<String, Integer> cache = QuestPoints.getLocalPoints();

            if(args.length == 2){
                player = Bukkit.getPlayerExact(commandSender.getName());
                update(player, cache, pointsToUpdate);
                return;
            }

            player = Bukkit.getPlayerExact(args[2]);

            if(player != null){
                update(player, cache, pointsToUpdate);
            } else{
                commandSender.sendMessage("That player could not be found.");
            }

        }catch(NumberFormatException e ){
            commandSender.sendMessage("Message improperly formatted! Please make sure to use this format: /qp add [points] [player]");
        }
    }

    private void update(Player player, HashMap<String, Integer> cache, int pointsToUpdate) {
        synchronized (this) {
            int oldPoints = cache.get(player.getUniqueId().toString().replaceAll("-", ""));
            int newPoints = oldPoints + pointsToUpdate;
            cache.put(player.getUniqueId().toString().replaceAll("-", ""), newPoints);
            QuestPoints.setLocalPoints(cache);
            QuestPoints.setNeedsUpdate(true);
        }
    }
}
