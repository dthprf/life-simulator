package com.codecool.Factory;

import com.codecool.Model.Board;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResourceSpawner extends Thread {

    private List<Resource> availableResources;
    private Board board;
    private int interval;
    private Random random;

    public ResourceSpawner(int interval, Board board) {
        this.interval = interval;
        this.board = board;
        this.random = new Random();
        this.availableResources = generateResources();
    }

    public void run() {
        try {
            while (!this.isInterrupted()) {
                Thread.sleep(interval);
                spawnResource();
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void spawnResource() {
        Resource toSpawn = availableResources.get(random.nextInt(availableResources.size()));
        Point spawnPoint = board.getRandomPoint();
        board.spawnResource(toSpawn, spawnPoint);
    }

    public void spawnCarrion(Point position, int energy) {
        Resource carrion = new Resource(energy, "carrion", "\uD83C\uDF56");
        board.spawnResource(carrion, position);
    }

    public void spawnMeat(Point position, int energy) {
        Resource meat = new Resource(energy, "meat", "\uD83C\uDF57");
        board.spawnResource(meat, position);
    }

    private List<Resource> generateResources() {
        List<Resource> resources = new ArrayList<>();
        Resource herb = new Resource(20, "herb", "\uD83C\uDF3F");
        Resource carrion = new Resource(20, "carrion", "\uD83C\uDF56");
        Resource meat = new Resource(30, "meat", "\uD83C\uDF57");
        Resource water = new Resource(40, "water", "\uD83D\uDEB0");

        // herb has ~42% chance of spawning
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);

        // water has ~25% chance of spawning
        resources.add(water);
        resources.add(water);
        resources.add(water);

        // carrion has ~25% chance of spawning
        resources.add(carrion);
        resources.add(carrion);
        resources.add(carrion);

        // meat has ~8% chance of spawning
        resources.add(meat);

        return resources;
    }
}
