package com.codecool.Factory;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.Herbivore;
import com.codecool.Model.MobData.Scavenger;
import com.codecool.Model.Point;

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
                    Point coordinates = board.getRandomPoint();
                    board.spawnElement(new Herbivore(coordinates, HERBIVORE_MOB), coordinates);
                }
                break;

            case PREDATOR_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = board.getRandomPoint();
                    board.spawnElement(new Predator(coordinates, PREDATOR_MOB), coordinates);
                }
                break;

            case SCAVENGER_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = board.getRandomPoint();
                    board.spawnElement(new Scavenger(coordinates, SCAVENGER_MOB), coordinates);
                }
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }

    public void spawnMob(Point coordinates, int health, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case HERBIVORE_MOB:
                board.spawnElement(new Herbivore(coordinates, HERBIVORE_MOB, health), coordinates);
                break;

            case PREDATOR_MOB:
                board.spawnElement(new Predator(coordinates, PREDATOR_MOB, health), coordinates);
                break;

            case SCAVENGER_MOB:
                board.spawnElement(new Scavenger(coordinates, SCAVENGER_MOB, health), coordinates);
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }
}
