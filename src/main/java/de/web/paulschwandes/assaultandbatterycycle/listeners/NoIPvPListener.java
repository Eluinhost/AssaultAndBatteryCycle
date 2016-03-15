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
