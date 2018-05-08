/*
 * Copyright (C) 2012-2018 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.dungeonsxl.reward;

import de.erethon.caliburn.item.VanillaItem;
import de.erethon.commons.gui.PageGUI;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.config.DMessage;
import de.erethon.dungeonsxl.player.DGlobalPlayer;
import de.erethon.dungeonsxl.player.DPermission;
import de.erethon.dungeonsxl.world.DEditWorld;
import de.erethon.dungeonsxl.world.DGameWorld;
import de.erethon.dungeonsxl.world.block.RewardChest;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * @author Frank Baumann, Daniel Saukel
 */
public class RewardListener implements Listener {

    DungeonsXL plugin = DungeonsXL.getInstance();

    /*@EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getPlayer();

        for (DLootInventory inventory : plugin.getDLootInventories()) {
            if (PageGUI.getByInventory() != inventory.getInventory()) {
                continue;
            }

            if (System.currentTimeMillis() - inventory.getTime() <= 500) {
                continue;
            }

            for (ItemStack istack : inventory.getInventory().getContents()) {
                if (istack != null) {
                    player.getWorld().dropItem(player.getLocation(), istack);
                }
            }

            plugin.getDLootInventories().remove(inventory);
        }
    }*/
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        InventoryView inventory = event.getView();

        DGameWorld gameWorld = DGameWorld.getByWorld(event.getPlayer().getWorld());

        if (gameWorld == null) {
            return;
        }

        if (!(inventory.getTopInventory().getHolder() instanceof Chest)) {
            return;
        }

        Chest chest = (Chest) inventory.getTopInventory().getHolder();

        for (RewardChest rewardChest : gameWorld.getRewardChests()) {
            if (!rewardChest.getChest().equals(chest)) {
                continue;
            }

            rewardChest.onOpen((Player) event.getPlayer());
            event.setCancelled(true);
        }

        if (!plugin.getMainConfig().getOpenInventories() && !DPermission.hasPermission(event.getPlayer(), DPermission.INSECURE)) {
            World world = event.getPlayer().getWorld();
            if (event.getInventory().getType() != InventoryType.CREATIVE && DEditWorld.getByWorld(world) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        DGlobalPlayer dPlayer = plugin.getDPlayers().getByPlayer(player);
        if (plugin.getDWorlds().getInstanceByWorld(player.getWorld()) != null) {
            return;
        }
        Block block = player.getLocation().getBlock();
        if (dPlayer.hasRewardItemsLeft() && !VanillaItem.PORTAL.is(block.getRelative(0, 1, 0)) && !VanillaItem.PORTAL.is(block.getRelative(0, -1, 0))
                && !VanillaItem.PORTAL.is(block.getRelative(1, 0, 0)) && !VanillaItem.PORTAL.is(block.getRelative(-1, 0, 0))
                && !VanillaItem.PORTAL.is(block.getRelative(0, 0, 1)) && !VanillaItem.PORTAL.is(block.getRelative(0, 0, -1))) {
            PageGUI lootInventory = new PageGUI(DMessage.PLAYER_TREASURES.getMessage(), true);
            for (ItemStack item : dPlayer.getRewardItems()) {
                if (item != null) {
                    lootInventory.addButton(item);
                }
            }
            lootInventory.open(player);
            dPlayer.setRewardItems(null);
        }
    }

}
