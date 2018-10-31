package com.codecool.Model.MobData;

import com.codecool.Model.Board;
import com.codecool.Model.Point;

public class Predator extends MobData {
    public Predator(Point position, String breed, Board board) {
        super(position, breed, board, "\uD83D\uDC3A");
        this.health += 1;
        this.damage += 5;
        this.speed += 4;
        this.foodList = new String[]{"water", "meat"};
    }
}
