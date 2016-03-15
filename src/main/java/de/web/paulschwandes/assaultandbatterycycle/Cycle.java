package de.web.paulschwandes.assaultandbatterycycle;

public enum Cycle {
    MELEE,
    RANGED,
    IPVP;

    public Cycle next() {
        Cycle[] values = values();
        int nextOrdinal = (this.ordinal()+1) % values.length;
        return values[nextOrdinal];
    }
}
