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
import de.web.paulschwandes.assaultandbatterycycle.CycleManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class FilterDamageListener implements Listener {

    protected final CycleManager manager;
    protected final Predicate<EntityDamageByEntityEvent> pvpDamageEventPredicate;

    public FilterDamageListener(
            CycleManager manager,
            Predicate<EntityDamageByEntityEvent> pvpDamageEventPredicate
    ) {
        this.manager = manager;
        this.pvpDamageEventPredicate = pvpDamageEventPredicate;
    }

    @EventHandler
    protected void on(EntityDamageByEntityEvent event) {
        if (!pvpDamageEventPredicate.apply(event)) {
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
