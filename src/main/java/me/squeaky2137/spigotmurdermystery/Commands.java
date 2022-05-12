package me.squeaky2137.spigotmurdermystery;

import me.squeaky2137.spigotmurdermystery.GameManagement.Game;
import me.squeaky2137.spigotmurdermystery.GameManagement.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor {

    public static Game game;

    public Commands(Main main) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("murder")) {
            if(args[0] == null)  {
                sender.sendMessage(ChatColor.RED + "Invalid Command");
                return true;
            }
            switch (args[0]) {
                case "start" -> {
                    game = new Game();
                    return true;
                }
                default -> {
                    sender.sendMessage(ChatColor.RED + "Invalid command");
                    return true;
                }
            }
        }
        return true;

    }
}
