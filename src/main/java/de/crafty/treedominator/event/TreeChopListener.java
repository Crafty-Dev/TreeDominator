package de.crafty.treedominator.event;

import de.crafty.treedominator.TreeDominator;
import de.crafty.treedominator.util.BlockUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreeChopListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack usedItem = event.getPlayer().getItemInHand();

        //Checking if the block is already included in another chopping process
        if (TreeDominator.CURRENTLY_BREAKING_BLOCKS.contains(block))
            return;


        if (!TreeDominator.VALID_LOGS.contains(block.getType().name()))
            return;

        if (TreeDominator.get().disableOnSneak() && player.isSneaking())
            return;

        if (TreeDominator.get().disableOnNotSneak() && !player.isSneaking())
            return;

        if (TreeDominator.get().requiresAxe() && !TreeDominator.VALID_TOOLS.contains(usedItem.getType().name()))
            return;


        //Determining all LogBlocks that belong to the tree/log-construct
        List<Block> allBlocks = BlockUtils.getAttachedBlocks(block);
        List<Block> tmpBlocks = new ArrayList<>(allBlocks);
        while (tmpBlocks.size() != 0) {
            List<Block> list = new ArrayList<>();

            tmpBlocks.forEach(b -> list.addAll(BlockUtils.getAttachedBlocks(b).stream().filter(b1 -> !allBlocks.contains(b1) && !list.contains(b1) && allBlocks.size() + list.size() != TreeDominator.get().maxBlocks()).toList()));

            allBlocks.addAll(list);
            tmpBlocks = list;
        }

        allBlocks.remove(block);

        TreeDominator.CURRENTLY_BREAKING_BLOCKS.addAll(allBlocks);

        //Starting the chopping process
        Random rand = new Random();
        BlockUtils.chop(allBlocks, 0, player, rand, block, usedItem);
    }


}
