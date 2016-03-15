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