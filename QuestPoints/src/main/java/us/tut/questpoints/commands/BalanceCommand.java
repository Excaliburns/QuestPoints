package us.tut.questpoints.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.tut.questpoints.QuestPoints;

public class BalanceCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(args.length == 0){
                System.out.println(player.getUniqueId().toString().replaceAll("-", ""));
                int points = QuestPoints.getLocalPoints().get(player.getUniqueId().toString().replaceAll("-", ""));
                commandSender.sendMessage(ChatColor.GOLD + "QuestPoints: " + points);
            }
        }

        return true;
    }
}
