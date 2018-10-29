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

    private Point drawCoordinatesForMob() {
        boolean areCoordinatesCorrect = false;
        Point coordinates = null;
        int maxY = board.getHeight();
        int maxX = board.getWidth();

        while(!areCoordinatesCorrect) {
            int randomY = ThreadLocalRandom.current().nextInt(0, maxY);
            int randomX = ThreadLocalRandom.current().nextInt(0, maxX);
            coordinates = new Point(randomX, randomY);
            areCoordinatesCorrect = board.isPointAvailableForMob(coordinates);
        }

        return coordinates;
    }
}
