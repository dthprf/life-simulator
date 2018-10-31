package com.codecool.Model.MobData;

import com.codecool.Model.Board;
import com.codecool.Model.Point;

public class Scavenger extends MobData {
    public Scavenger(Point position, String breed, Board board) {
        super(position, breed, board);
        this.damage += 1;
        this.speed += 9;
        this.foodList = new String[]{"water", "carrion", "meat"};
    }
}
