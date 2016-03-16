/*
 The MIT License (MIT)
 Copyright (c) 2016 Paul Schwandes (paulschwandes@web.de)

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package de.web.paulschwandes.assaultandbatterycycle.listeners;

import com.google.common.base.Predicate;
import de.web.paulschwandes.assaultandbatterycycle.Cycle;
import de.web.paulschwandes.assaultandbatterycycle.CycleManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class NoIPvPListener implements Listener {

    protected final CycleManager manager;
    protected final Predicate<Material> iPvPMaterialPredicate;
    protected final double disabledRange;

    public NoIPvPListener(
            CycleManager manager,
            Predicate<Material> iPvPMaterialPredicate,
            double disabledRange
    ) {
        this.manager = manager;
        this.iPvPMaterialPredicate = iPvPMaterialPredicate;
        this.disabledRange = disabledRange;
    }

    protected void handleEvent(Player player, Location blockLocation, Material material, Cancellable event) {
        if (manager.getCurrentCycle() == Cycle.IPVP) {
            return;
        }

        if (!iPvPMaterialPredicate.apply(material)) {
            return;
        }

        for (Player otherPlayer : blockLocation.getWorld().getPlayers()) {
            if (player == otherPlayer) {
                continue;
            }

            Location otherPlayerLocation = otherPlayer.getLocation();
            double distance = blockLocation.distance(otherPlayerLocation);
            if (distance > disabledRange) {
                continue;
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    protected void on(BlockPlaceEvent event) {
        Block block = event.getBlock();
        handleEvent(event.getPlayer(), block.getLocation(), block.getType(), event);
    }

    @EventHandler
    protected void on(PlayerBucketEmptyEvent event) {
        Block blockClicked = event.getBlockClicked();
        BlockFace blockFace = event.getBlockFace();
        Block placedBlock = blockClicked.getRelative(blockFace);
        handleEvent(event.getPlayer(), placedBlock.getLocation(), event.getBucket(), event);
    }
}
