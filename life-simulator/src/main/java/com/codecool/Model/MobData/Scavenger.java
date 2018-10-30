package com.codecool.Model.MobData;

import com.codecool.Model.Point;

public class Scavenger extends MobData {
    public Scavenger(Point position, String breed) {
        super(position, breed);
        this.damage += 3;
        this.speed += 7;
    }
}
