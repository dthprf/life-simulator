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
                wait(interval);
                spawnResource();
            }
        } catch (InterruptedException e) {
            System.out.println("Spawner Stopped");
        }
    }

    private void spawnResource() {
        Resource toSpawn = availableResources.get(random.nextInt(availableResources.size()));
        Point spawnPoint = board.getRandomPoint();
        board.spawnResource(toSpawn, spawnPoint);
    }

    public void spawnCarrion(Point position, int energy) {
        Resource carrion = new Resource(energy, "carrion");
        board.spawnResource(carrion, position);
    }

    private List<Resource> generateResources() {
        List<Resource> resources = new ArrayList<>();
        Resource herb = new Resource(5, "herb");
        Resource carrion = new Resource(7, "carrion");
        Resource meat = new Resource(10, "meat");
        Resource water = new Resource(20, "water");

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
