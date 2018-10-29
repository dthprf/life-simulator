package com.codecool.Model;

public class Resource {
    private int energy;
    private String name;

    public Resource(int energy, String name) {
        this.energy = energy;
        this.name = name.toUpperCase();
    }

    public int getEnergy() {
        return energy;
    }

    public String getName() {
        return name;
    }
}
