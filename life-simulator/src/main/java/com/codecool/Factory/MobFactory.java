package com.codecool.Factory;

import com.codecool.Constant.MobTypes;
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
    private Board board;
    private ResourceSpawner spawner;

    public MobFactory(Board board, ResourceSpawner spawner) {
        this.board = board;
        this.spawner = spawner;
    }

    public void spawnMob(int number, String type) throws UnrecognizedMobBreedException {
        switch (type) {
            case MobTypes.HERBIVORE_MOB:
                for (int i = 0; i < number; i++) {
                    MobThread newMob = createHerbivore();
                    board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
                    Thread thread = new Thread(newMob);
                    thread.start();
                }
                break;

            case MobTypes.PREDATOR_MOB:
                for (int i = 0; i < number; i++) {
                    MobThread newMob = createPredator();
                    board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
                    Thread thread = new Thread(newMob);
                    thread.start();
                }
                break;

            case MobTypes.SCAVENGER_MOB:
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

    public void spawnMob(Point coordinates, int energy, String type) throws UnrecognizedMobBreedException {
        MobThread newMob;
        switch (type) {
            case MobTypes.HERBIVORE_MOB:
                newMob = createHerbivore();
                break;

            case MobTypes.PREDATOR_MOB:
                newMob = createPredator();
                break;

            case MobTypes.SCAVENGER_MOB:
                newMob = createScavenger();
                break;

            default:
                throw new UnrecognizedMobBreedException(type + " is not available.");
        }
        newMob.getMobData().setPosition(coordinates);
        newMob.getMobData().setEnergy(energy);
        board.spawnMob(newMob.getMobData(), newMob.getMobData().getPosition());
        Thread thread = new Thread(newMob);
        thread.start();
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

    private MobThread createScavenger() {
        MobData data = new Scavenger(drawCoordinates(), MobTypes.SCAVENGER_MOB, this.board);
        MobBehaviour behaviour = new ScavengerBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
    }

    private MobThread createPredator() {
        MobData data = new Predator(drawCoordinates(), MobTypes.PREDATOR_MOB, this.board);
        MobBehaviour behaviour = new PredatorBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
    }

    private MobThread createHerbivore() {
        MobData data = new Herbivore(drawCoordinates(), MobTypes.HERBIVORE_MOB, this.board);
        MobBehaviour behaviour = new HerbivoreBehaviour(this, data);
        return new MobThread(data, behaviour, spawner);
    }
}
