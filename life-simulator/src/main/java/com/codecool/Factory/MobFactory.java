package com.codecool.Factory;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.Herbivore;
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

    public void spawnMob(int number, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case HERBIVORE_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = drawCoordinatesForMob();
                    board.spawnElement(coordinates, new Herbivore(coordinates, HERBIVORE_MOB));
                }
                break;

            case PREDATOR_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = drawCoordinatesForMob();
                    board.spawnElement(coordinates, new Predator(coordinates, PREDATOR_MOB));
                }
                break;

            case SCAVENGER_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = drawCoordinatesForMob();
                    board.spawnElement(coordinates, new Scavenger(coordinates, SCAVENGER_MOB));
                }
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }

    public void spawnMob(Point coordinates, int health, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case HERBIVORE_MOB:
                board.spawnElement(coordinates, health, HERBIVORE_MOB);
                break;

            case PREDATOR_MOB:
                board.spawnElement(coordinates, health, PREDATOR_MOB);
                break;

            case SCAVENGER_MOB:
                board.spawnElement(coordinates, health, SCAVENGER_MOB);
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
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
