package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.base.Predicate;
import org.bukkit.Material;

public class IPvPMaterialPredicate implements Predicate<Material> {

    @Override
    public boolean apply(Material material) {
        if (material.hasGravity()) {
            return true;
        }

        switch (material) {
            case FIRE:
            case LAVA:
            case LAVA_BUCKET:
            case STATIONARY_LAVA:
                return true;
            default:
                return false;
        }
    }
}
