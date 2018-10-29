package com.codecool.Model.MobData;

import com.codecool.Model.Point;

import java.util.List;

public class Herbivor extends MobData{

    public Herbivor(Point position, String breed, List<String> foodList) {
        super(position, breed, foodList);
        this.health += 5;
        this.damage += 2;
        this.speed += 3;
    }
}
