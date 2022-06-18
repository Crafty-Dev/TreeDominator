package de.crafty.treedominator.cmd;

import de.crafty.treedominator.util.DecayHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static de.crafty.treedominator.TreeDominator.PREFIX;

/**
 * The basic TreeDominator Command<br>
 * Currently only used for reloading the DecayHandler
 */
public class CMD_treedominator implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player player))
            return false;

        if (!cmd.getName().equalsIgnoreCase("treedominator"))
            return false;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.sendMessage(PREFIX + "\u00a73 Worlds are reloading! \u00a7oThe Server may lag for a few seconds"));
                player.sendMessage(PREFIX + "\u00a7aReloading Worlds depending on Config...");

                List<World> worlds = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (!worlds.contains(p.getWorld()))
                        worlds.add(p.getWorld());
                });
                worlds.forEach(DecayHandler::updateDecayingLeavesByWorld);
                player.sendMessage(PREFIX + "\u00a7aReload complete!");
                Bukkit.getOnlinePlayers().stream().filter(p -> !p.equals(player)).forEach(p -> p.sendMessage(PREFIX + "\u00a73Reload complete!"));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> list = new ArrayList<>();

        if (!(sender instanceof Player player))
            return list;

        if (!cmd.getName().equalsIgnoreCase("treedominator"))
            return list;


        if (args.length == 1) {
            if ("reload".startsWith(args[0]))
                list.add("reload");
        }
        return list;
    }
}
