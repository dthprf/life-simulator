package com.codecool.Model;

public class Resource {
    private int energy;
    private String name;
    private String asText;

    public Resource(int energy, String name, String unicode) {
        this.energy = energy;
        this.name = name.toLowerCase();
        this.asText = unicode;
    }

    public int getEnergy() {
        return energy;
    }

    public String getName() {
        return name;
    }

    public String getAsText() {
        return asText;
    }
}
