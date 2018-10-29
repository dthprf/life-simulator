package com.codecool.Model.MobData;

import com.codecool.Model.Point;

import java.util.List;

public class Herbivor extends MobData{

    public Herbivor(Point position, int health, int speed, int damage, String breed, List<String> foodList) {
        super(position, health, speed, damage, breed, foodList);
    }
}
