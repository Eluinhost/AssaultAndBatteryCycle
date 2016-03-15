package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.base.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PvPDamagePredicate implements Predicate<EntityDamageByEntityEvent> {

    @Override
    public boolean apply(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (!(damaged instanceof Player)) {
            return false;
        }

        Entity damager = event.getDamager();
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            ProjectileSource source = projectile.getShooter();
            return source instanceof Player;
        }

        return damager instanceof Player;
    }
}
