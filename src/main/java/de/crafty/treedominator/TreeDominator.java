package de.crafty.treedominator;

import de.crafty.treedominator.cmd.CMD_treedominator;
import de.crafty.treedominator.event.LeafDecayListener;
import de.crafty.treedominator.event.TreeChopListener;
import de.crafty.treedominator.util.BlockUtils;
import de.crafty.treedominator.util.DecayHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeDominator extends JavaPlugin {

    public static final String PREFIX = "\u00a77[\u00a75TreeDominator\u00a77] ";
    /**
     * A list of currently breaking Blocks; Important for preventing a chain reaction of the chopping mechanism<br>
     * See {@link TreeChopListener}
     */
    public static final List<Block> CURRENTLY_BREAKING_BLOCKS = new ArrayList<>();

    /**
     * A list of valid LogBlocks <br>
     * Saved as a list of string for version independent comparison
     */
    public static final List<String> VALID_LOGS = new ArrayList<>();
    /**
     * A list of valid Tools that can be used for Tree Chopping <br>
     * Saved as a list of string for version independent comparison
     */
    public static final List<String> VALID_TOOLS = new ArrayList<>();

    private static TreeDominator instance;

    @Override
    public void onEnable() {

        instance = this;
        Bukkit.getScheduler().getPendingTasks().forEach(BukkitTask::cancel);

        Bukkit.getPluginManager().registerEvents(new TreeChopListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeafDecayListener(), this);

        this.getCommand("treedominator").setExecutor(new CMD_treedominator());

        this.saveDefaultConfig();

        DecayHandler.init();

        VALID_LOGS.clear();
        VALID_TOOLS.clear();
        
        VALID_LOGS.addAll(this.validLogs());
        VALID_TOOLS.addAll(this.validTools());

        Bukkit.getConsoleSender().sendMessage(PREFIX + "Plugin enabled");
    }


    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX + "Plugin disabled");
    }


    public int maxBlocks() {
        return this.getConfig().getInt("maxBlocks");
    }

    public boolean disableOnSneak() {
        return this.getConfig().getBoolean("disableOnSneak");
    }

    public boolean disableOnNotSneak() {
        return this.getConfig().getBoolean("disableOnNotSneak");
    }

    public boolean requiresAxe() {
        return this.getConfig().getBoolean("requiresAxe");
    }

    public boolean delayedBreaking() {
        return this.getConfig().getBoolean("delayedBreaking");
    }

    public boolean dropTogether() {
        return this.getConfig().getBoolean("dropTogether");
    }

    public boolean fastLeafDecay() {
        return this.getConfig().getBoolean("fastLeafDecay");
    }

    public List<String> validLogs(){
        return this.getConfig().getStringList("validLogs");
    }

    public List<String> validTools(){
        return this.getConfig().getStringList("validTools");
    }

    public static TreeDominator get() {
        return instance;
    }

}
