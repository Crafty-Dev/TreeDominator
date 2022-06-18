package de.crafty.treedominator.event;

import de.crafty.treedominator.TreeDominator;
import de.crafty.treedominator.util.DecayHandler;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeafDecayListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (!TreeDominator.get().fastLeafDecay())
            return;

        Block block = event.getBlock();
        if (TreeDominator.VALID_LOGS.contains(block.getType().name()))
            DecayHandler.updateDecayingLeavesByBlock(block.getX(), block.getY(), block.getZ(), block.getWorld());
    }

}
