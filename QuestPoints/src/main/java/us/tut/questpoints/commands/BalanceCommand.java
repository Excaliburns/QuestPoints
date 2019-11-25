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

                int points = QuestPoints.getLocalPoints().get(player.getUniqueId().toString().replaceAll("-", ""));
                commandSender.sendMessage(ChatColor.GOLD + "[QuestPoints] Balance: " + ChatColor.DARK_GREEN + points);

            }
            else if(args[0].toLowerCase().equals("add") | args[0].toLowerCase().equals("remove")) {
                new UpdatePoints(commandSender, command, args);
            }
            else{
                player.sendMessage("Message is improperly formatted. Please use /qp add/remove [points] [player].");
            }

        }

        return true;
    }
}
