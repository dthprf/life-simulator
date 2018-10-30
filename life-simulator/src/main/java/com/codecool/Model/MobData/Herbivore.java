package com.codecool.Model.MobData;

import com.codecool.Model.Point;

import java.util.List;

public class Herbivore extends MobData{

    public Herbivore(Point position, String breed) {
        super(position, breed);
        this.health += 5;
        this.damage += 2;
        this.speed += 3;
    }

    public Herbivore(Point position, String breed, int health) {
        super(position, breed);
        this.health = health;
        this.damage += 2;
        this.speed += 3;
    }
}
