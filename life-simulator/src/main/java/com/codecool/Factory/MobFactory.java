package com.codecool.Factory;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.MobBehaviour.HerbivoreBehaviour;
import com.codecool.MobBehaviour.MobBehaviour;
import com.codecool.MobBehaviour.PredatorBehaviour;
import com.codecool.MobBehaviour.ScavengerBehaviour;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.Herbivore;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.MobData.Predator;
import com.codecool.Model.MobData.Scavenger;
import com.codecool.Model.Point;
import com.codecool.Thread.MobThread;

public class MobFactory {
    private static final String PREDATOR_MOB = "predator";
    private static final String HERBIVORE_MOB = "herbivore";
    private static final String SCAVENGER_MOB = "scavenger";

    private Board board;
    private ResourceSpawner spawner;

    public MobFactory(Board board, ResourceSpawner spawner) {
        this.board = board;
        this.spawner = spawner;
    }

    public void spawnMob(int number, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case HERBIVORE_MOB:
                for (int i = 0; i < number; i++) {
                    MobThread newMob = createHerbivore();
                    board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
                    Thread thread = new Thread(newMob);
                    thread.start();
                }
                break;

            case PREDATOR_MOB:
                for (int i = 0; i < number; i++) {
                    MobThread newMob = createPredator();
                    board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
                    Thread thread = new Thread(newMob);
                    thread.start();
                }
                break;

            case SCAVENGER_MOB:
                for (int i = 0; i < number; i++) {
                    MobThread newMob = createScavenger();
                    board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
                    Thread thread = new Thread(newMob);
                    thread.start();
                }
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
    }

    private MobThread createScavenger() {
        MobData data = new Scavenger(drawCoordinates(), SCAVENGER_MOB, this.board);
        MobBehaviour behaviour = new ScavengerBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
    }

    private MobThread createPredator() {
        MobData data = new Predator(drawCoordinates(), PREDATOR_MOB, this.board);
        MobBehaviour behaviour = new PredatorBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
    }

    private MobThread createHerbivore() {
        MobData data = new Herbivore(drawCoordinates(), HERBIVORE_MOB, this.board);
        MobBehaviour behaviour = new HerbivoreBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
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

    private Point drawCoordinates() {
        boolean isCoordinateAvailable = false;
        Point coordinates = null;

        while (!isCoordinateAvailable) {
            coordinates = board.getRandomPoint();
            isCoordinateAvailable = board.isPointAvailableForMob(coordinates);
        }

        return coordinates;
    }
}
