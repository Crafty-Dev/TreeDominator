package de.crafty.treedominator;

import de.crafty.treedominator.cmd.CMD_treedominator;
import de.crafty.treedominator.event.LeafDecayListener;
import de.crafty.treedominator.event.TreeChopListener;
import de.crafty.treedominator.util.BlockUtils;
import de.crafty.treedominator.util.DecayHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

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
    public static final List<String> VALID_LOGS = Arrays.asList(
            "OAK_LOG",
            "BIRCH_LOG",
            "DARK_OAK_LOG",
            "ACACIA_LOG",
            "JUNGLE_LOG",
            "SPRUCE_LOG",
            "MANGROVE_LOG",
            "CRIMSON_STEM",
            "WARPED_STEM"
    );
    /**
     * A list of valid Tools that can be used for Tree Chopping <br>
     * Saved as a list of string for version independent comparison
     */
    public static final List<String> VALID_TOOLS = Arrays.asList(
            "WOODEN_AXE",
            "STONE_AXE",
            "IRON_AXE",
            "GOLDEN_AXE",
            "DIAMOND_AXE",
            "NETHERITE_AXE"
    );

    private static TreeDominator instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getPluginManager().registerEvents(new TreeChopListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeafDecayListener(), this);

        this.getCommand("treedominator").setExecutor(new CMD_treedominator());

        this.saveDefaultConfig();

        DecayHandler.init();

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

    public static TreeDominator get() {
        return instance;
    }
}
