package de.crafty.treedominator.util;

import de.crafty.treedominator.TreeDominator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.block.LeavesDecayEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class DecayHandler {

    /**
     * A list of leaves that are currently decaying<br>
     * TODO remove blocks that are still connected to a log
     */
    private static final List<Block> TICKING_LEAVES = new ArrayList<>();


    public static boolean isBlockDecayingLeaf(Block block) {
        return (block.getBlockData() instanceof Leaves leaves) && !leaves.isPersistent() && leaves.getDistance() >= 7;
    }

    /**
     * Checks for decaying leaves depending on a world
     * @param world The world
     */
    public static void updateDecayingLeavesByWorld(World world) {

        for (Chunk chunk : world.getLoadedChunks())
            updateDecayingLeavesByChunk(chunk);

    }

    /**
     * Checks for decaying leaves depending on a chunk
     * @param chunk The chunk
     */
    public static void updateDecayingLeavesByChunk(Chunk chunk) {

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getWorld().getMinHeight(); y < chunk.getWorld().getMaxHeight() + 1; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (isBlockDecayingLeaf(block) && !TICKING_LEAVES.contains(block))
                        TICKING_LEAVES.add(block);
                }
            }
        }

    }

    /**
     * Checks for decaying leaves depending on a LogBlock
     * @param x X-Pos of the LogBlock
     * @param y Y-Pos of the LogBlock
     * @param z Z-Pos of the LogBlock
     * @param world The world of the LogBlock
     */
    public static void updateDecayingLeavesByBlock(int x, int y, int z, World world) {

        for (int yOffset = -6; yOffset <= 6; yOffset++) {
            int i = (int) Math.sqrt(Math.pow(yOffset, 2));
            int maxXOffset = 6 - i;
            for (int xOffset = -maxXOffset; xOffset <= maxXOffset; xOffset++) {
                int j = (int) Math.sqrt(Math.pow(xOffset, 2));
                int maxZOffset = 6 - j - i;

                for (int zOffset = -maxZOffset; zOffset <= maxZOffset; zOffset++) {
                    int newX = x + xOffset;
                    int newY = y + yOffset;
                    int newZ = z + zOffset;

                    Block b = world.getBlockAt(newX, newY, newZ);


                    if (b.getBlockData() instanceof Leaves leaves && !leaves.isPersistent() && !TICKING_LEAVES.contains(b)) {
                        TICKING_LEAVES.add(b);
                    }

                }
            }
        }

    }


    /**
     * The actual decaying handler<br>
     * Uses a {@link Random} for a smoother leaf decaying
     */
    public static void init() {

        if (!TreeDominator.get().fastLeafDecay())
            return;

        Random rand = new Random();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(TreeDominator.get(), () -> {

            TICKING_LEAVES.removeAll(TICKING_LEAVES.stream().filter(block -> !(block.getBlockData() instanceof Leaves)).toList());

            List<Block> toRemove = new ArrayList<>();
            TICKING_LEAVES.forEach(block -> {
                Leaves leaves = (Leaves) block.getBlockData();
                if (leaves.getDistance() == 7 && rand.nextInt(5) == 0) {
                    LeavesDecayEvent event = new LeavesDecayEvent(block);
                    Bukkit.getPluginManager().callEvent(event);
                    if(!event.isCancelled()){
                        block.breakNaturally();
                        toRemove.add(block);
                    }
                }
            });

            TICKING_LEAVES.removeAll(toRemove);

        }, 0, 1);

    }

}
