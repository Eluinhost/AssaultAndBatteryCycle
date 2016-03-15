package de.web.paulschwandes.assaultandbatterycycle;

import org.junit.Test;

import static org.junit.Assert.*;

public class CycleTest {

    @Test
    public void testNext() throws Exception {
        assertTrue(Cycle.MELEE.next() == Cycle.RANGED);
        assertTrue(Cycle.RANGED.next() == Cycle.IPVP);
        assertTrue(Cycle.IPVP.next() == Cycle.MELEE);
    }
}