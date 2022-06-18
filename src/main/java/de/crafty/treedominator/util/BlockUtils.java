package de.crafty.treedominator.util;

import de.crafty.treedominator.TreeDominator;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockUtils {


      /**
     *A recursive function handling the whole process of Tree chopping
     * @param blockList The list of logs that have to get chopped
     * @param current The id of the log that should get chopped in the current iteration
     * @param player The player who has chopped down the tree
     * @param rand A {@link Random} for calculating the items durability
     * @param src The block that has orginally been broken
     * @param usedItem The Item the player used
     */
    public static void chop(List<Block> blockList, int current, Player player, Random rand, Block src, ItemStack usedItem) {

        if (current >= blockList.size())
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(TreeDominator.get(), () -> {

            Block b = blockList.get(current);

            if (TreeDominator.VALID_LOGS.contains(b.getType().name())) {
                if (usedItem.getItemMeta() instanceof Damageable meta) {


                    if (meta.getDamage() > usedItem.getType().getMaxDurability()) {
                        player.getInventory().remove(usedItem);
                        player.playSound(player, Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);
                        return;
                    }

                    int i = meta.getEnchantLevel(Enchantment.DURABILITY);
                    if (rand.nextInt(i + 1) <= 0 && player.getGameMode() != GameMode.CREATIVE)
                        meta.setDamage(meta.getDamage() + 1);

                    usedItem.setItemMeta(meta);
                }

                if (TreeDominator.get().delayedBreaking())
                    player.getWorld().playSound(b.getLocation(), (b.getType() == Material.CRIMSON_STEM || b.getType() == Material.WARPED_STEM) ? Sound.BLOCK_STEM_BREAK : Sound.BLOCK_WOOD_BREAK, 1.0F, 1.0F);


                BlockBreakEvent event = new BlockBreakEvent(b, player);
                Bukkit.getPluginManager().callEvent(event);

                World world = b.getWorld();

                if (event.isDropItems() && !event.isCancelled() && player.getGameMode() != GameMode.CREATIVE) {
                    if (TreeDominator.get().dropTogether())
                        b.getDrops().forEach(stack -> world.dropItem(src.getLocation(), stack));
                    else
                        b.getDrops().forEach(stack -> world.dropItemNaturally(b.getLocation(), stack));
                }

                if (!event.isCancelled())
                    world.setBlockData(b.getLocation(), Material.AIR.createBlockData());
            }
            TreeDominator.CURRENTLY_BREAKING_BLOCKS.remove(b);
            chop(blockList, current + 1, player, rand, src, usedItem);
        }, TreeDominator.get().delayedBreaking() ? 1 : 0);
    }

    /**
     *Determines attached Blocks in an one block radius including diagonals
     * @param block The Block from which the attached blocks should get determined
     * @return A list of blocks that surround the original block
     */
    public static List<Block> getAttachedBlocks(Block block) {
        List<Block> blocks = new ArrayList<>();

        World world = block.getWorld();

        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (world.getBlockAt(x + j, y + i, z + k).getType() == block.getType())
                        blocks.add(world.getBlockAt(x + j, y + i, z + k));
                }
            }
        }
        return blocks;
    }

}
