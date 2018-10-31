package com.codecool.Model.MobData;

import com.codecool.Model.Board;
import com.codecool.Model.Point;


public class Herbivore extends MobData {

    public Herbivore(Point position, String breed, Board board) {
        super(position, breed, board, "\uD83D\uDC18");
        this.health += 5;
        this.damage += 2;
        this.speed += 3;
        this.foodList = new String[]{"water", "herb"};
    }
}
