package com.codecool.Factory;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.Herbivore;
import com.codecool.Model.MobData.MobData;
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
                    board.spawnMob(new Herbivore(coordinates, HERBIVORE_MOB, this.board), coordinates);
                }
                break;

            case PREDATOR_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = board.getRandomPoint();
                    board.spawnMob(new Predator(coordinates, PREDATOR_MOB, this.board), coordinates);
                }
                break;

            case SCAVENGER_MOB:
                for (int i = 0; i < number; i++) {
                    Point coordinates = board.getRandomPoint();
                    board.spawnMob(new Scavenger(coordinates, SCAVENGER_MOB, this.board), coordinates);
                }
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }

    public void spawnMob(Point coordinates, int energy, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case HERBIVORE_MOB:
                MobData herbivoreMob = new Herbivore(coordinates, HERBIVORE_MOB, this.board);
                herbivoreMob.setEnergy(energy);
                board.spawnMob(herbivoreMob, coordinates);
                break;

            case PREDATOR_MOB:
                MobData predatorMob = new Predator(coordinates, PREDATOR_MOB, this.board);
                predatorMob.setEnergy(energy);
                board.spawnMob(predatorMob, coordinates);
                break;

            case SCAVENGER_MOB:
                MobData scavengerMob = new Scavenger(coordinates, SCAVENGER_MOB, this.board);
                scavengerMob.setEnergy(energy);
                board.spawnMob(scavengerMob, coordinates);
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }
}
