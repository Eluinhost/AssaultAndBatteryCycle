package de.web.paulschwandes.assaultandbatterycycle.listeners;

import com.google.common.base.Predicate;
import de.web.paulschwandes.assaultandbatterycycle.CycleManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class FilterDamageListener implements Listener {

    protected final CycleManager manager;
    protected final Predicate<EntityDamageByEntityEvent> pvpDamagePredicate;

    public FilterDamageListener(CycleManager manager, Predicate<EntityDamageByEntityEvent> pvpDamagePredicate) {
        this.manager = manager;
        this.pvpDamagePredicate = pvpDamagePredicate;
    }

    @EventHandler
    protected void on(EntityDamageByEntityEvent event) {
        if (!pvpDamagePredicate.apply(event)) {
            return;
        }

        EntityDamageEvent.DamageCause cause = event.getCause();
        switch (manager.getCurrentCycle()) {
            case MELEE:
                if (cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setCancelled(true);
                break;
            case RANGED:
                if (cause != EntityDamageEvent.DamageCause.PROJECTILE) event.setCancelled(true);
                break;
            case IPVP:
                event.setCancelled(true);
                break;
        }
    }

}
