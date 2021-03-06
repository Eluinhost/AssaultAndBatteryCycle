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

package de.web.paulschwandes.assaultandbatterycycle;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PvPDamageEventPredicateTest {

    protected final PvPDamageEventPredicate predicate = new PvPDamageEventPredicate();

    protected void testEvent(Entity victim, Entity attacker, boolean expectedResult) {
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
        when(event.getEntity()).thenReturn(victim);
        when(event.getDamager()).thenReturn(attacker);
        assertEquals(predicate.apply(event), expectedResult);
    }
    @Test
    public void testVictimIsNotPlayer() throws Exception {
        Creeper victim = mock(Creeper.class);
        testEvent(victim, null, false);
    }

    @Test
    public void testAttackerIsNotPlayer() throws Exception {
        Player victim = mock(Player.class);
        Creeper attacker = mock(Creeper.class);
        testEvent(victim, attacker, false);
    }

    @Test
    public void testProjectileFromSkeleton() throws Exception {
        Player victim = mock(Player.class);
        Skeleton attacker = mock(Skeleton.class);
        Arrow arrow = mock(Arrow.class);
        when(arrow.getShooter()).thenReturn(attacker);
        testEvent(victim, arrow, false);
    }

    @Test
    public void testMeleePvP() throws Exception {
        Player victim = mock(Player.class);
        Player attacker = mock(Player.class);
        testEvent(victim, attacker, true);
    }

    @Test
    public void testRangedPvP() {
        Player victim = mock(Player.class);
        Player attacker = mock(Player.class);
        Arrow arrow = mock(Arrow.class);
        when(arrow.getShooter()).thenReturn(attacker);
        testEvent(victim, arrow, true);
    }
}