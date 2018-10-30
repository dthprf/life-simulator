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

    private List<Resource> generateResources() {
        List<Resource> resources = new ArrayList<>();
        Resource herb = new Resource(5, "herb");
        Resource carrion = new Resource(7, "carrion");
        Resource meat = new Resource(10, "meat");

        // herb has ~63% chance of spawning
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);
        resources.add(herb);

        // carrion has ~25% chance of spawning
        resources.add(carrion);
        resources.add(carrion);

        // meat has ~13% chance of spawning
        resources.add(meat);

        return resources;
    }
}
