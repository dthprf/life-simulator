package com.codecool.Factory;

import com.codecool.Model.Board;
import com.codecool.Model.Point;

import java.util.concurrent.ThreadLocalRandom;


public class MobFactory {
    private static final String PREDATOR_MOB = "predator";
    private static final String HERBIVORE_MOB = "herbivore";
    private static final String SCAVENGER_MOB = "scavenger";

    private Board board;

    public MobFactory(Board board) {
        this.board = board;
    }

    public void spawnMob(int number, String type) {
        
    }

    public void spawnMob(Point coordinates, int health) {

    }

    private Point drawCoordinates() {
        int maxY = board.getHeight();
        int maxX = board.getWidth();

        int randomY = ThreadLocalRandom.current().nextInt(1, maxY);
        int randomX = ThreadLocalRandom.current().nextInt(1, maxX);



    }

    private boolean isCoordinateOcupied(Point point) {

    }
}
