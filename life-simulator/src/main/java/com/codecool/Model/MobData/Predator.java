package com.codecool.Model.MobData;

import com.codecool.Model.Board;
import com.codecool.Model.Point;

public class Predator extends MobData {
    public Predator(Point position, String breed, Board board) {
        super(position, breed, board);
        this.damage += 5;
        this.speed += 5;
        this.foodList = new String[]{"water", "meat", "herbivour"};
    }
}
