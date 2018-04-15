package com.gatheringhallstudios.mhworlddatabase.data.views;

/**
 * Represents information for a single reward
 */
public class Reward {
    // todo: see if there's a way to make these private/protected + getters only without room complaining

    public int id; // Item ID
    public String name;
    public String condition; // TODO Change to enum?
    public String rank; // TODO Change to enum?
    public int stackSize;
    public int percentage;
}
