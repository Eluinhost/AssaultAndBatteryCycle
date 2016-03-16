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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class FilterDamageListenerTest {

    protected FilterDamageListener listener;
    protected CycleManager cycleManager;
    protected EntityDamageByEntityEvent event;

    protected boolean isPvP;
    protected Cycle cycle;
    protected EntityDamageEvent.DamageCause cause;

    @Before
    public void setUp() throws Exception {
        cycle = null;
        cycleManager = mock(CycleManager.class);

        when(cycleManager.getCurrentCycle()).thenAnswer(new Answer<Cycle>() {
            @Override
            public Cycle answer(InvocationOnMock invocationOnMock) throws Throwable {
                return cycle;
            }
        });

        isPvP = true;
        Predicate<EntityDamageByEntityEvent> predicate = new Predicate<EntityDamageByEntityEvent>() {
            @Override
            public boolean apply(EntityDamageByEntityEvent input) {
                return isPvP;
            }
        };

        cause = null;
        event = mock(EntityDamageByEntityEvent.class);
        when(event.getCause()).thenAnswer(new Answer<EntityDamageEvent.DamageCause>() {
            @Override
            public EntityDamageEvent.DamageCause answer(InvocationOnMock invocationOnMock) throws Throwable {
                return cause;
            }
        });

        listener = new FilterDamageListener(cycleManager, predicate);
    }

    @Test
    public void testNonPVP() {
        isPvP = false;

        listener.on(event);

        verifyZeroInteractions(event);
        verifyZeroInteractions(cycleManager);
    }

    @Test
    public void testMeleeEntityAttack() {
        cycle = Cycle.MELEE;
        cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

        listener.on(event);

        verify(event, never()).setCancelled(true);
    }

    @Test
    public void testMeleeOthers() {
        cycle = Cycle.MELEE;

        for (EntityDamageEvent.DamageCause c : EntityDamageEvent.DamageCause.values()) {
            if (c == EntityDamageEvent.DamageCause.ENTITY_ATTACK) continue;

            cause = c;
            listener.on(event);
        }

        verify(event, times(EntityDamageEvent.DamageCause.values().length - 1)).setCancelled(true);
    }

    @Test
    public void testRangedProjectile() {
        cycle = Cycle.RANGED;
        cause = EntityDamageEvent.DamageCause.PROJECTILE;

        listener.on(event);

        verify(event, never()).setCancelled(true);
    }

    @Test
    public void testRangedOthers() {
        cycle = Cycle.RANGED;

        for (EntityDamageEvent.DamageCause c : EntityDamageEvent.DamageCause.values()) {
            if (c == EntityDamageEvent.DamageCause.PROJECTILE) continue;

            cause = c;
            listener.on(event);
        }

        verify(event, times(EntityDamageEvent.DamageCause.values().length - 1)).setCancelled(true);
    }

    @Test
    public void testIPVP() {
        cycle = Cycle.IPVP;

        listener.on(event);

        verify(event, times(1)).setCancelled(true);
    }
}
