package com.codecool.Thread;

import com.codecool.Factory.ResourceSpawner;
import com.codecool.MobBehaviour.MobBehaviour;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

public class MobThread implements Runnable {
    private MobData mobData;
    private MobBehaviour mobBehaviour;
    private ResourceSpawner resourceSpawner;

    public MobThread(MobData mobData, MobBehaviour mobBehaviour, ResourceSpawner resourceSpawner) {
        this.mobData = mobData;
        this.mobBehaviour = mobBehaviour;
        this.resourceSpawner = resourceSpawner;
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        while (!t.isInterrupted() && !diedDueToDamage() && !diedDueToLackOfFood()) {
            try {
                mobBehaviour.update();
                Thread.sleep(10000 / mobData.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
//        System.out.println("MOB DIED " + mobData.getBreed());
        if (diedDueToDamage()) {
            spawnMeat();
        } else if (diedDueToLackOfFood()) {
            spawnCarrion();
        }

        removeDataFromBoard();
    }

    private void removeDataFromBoard() {
        Board board = mobData.getBoard();
        Point position = mobData.getPosition();
        board.getBoard().get(position).removeMob(mobData);
    }

    private boolean diedDueToLackOfFood() {
        return mobData.getEnergy() <= 0;
    }

    private boolean diedDueToDamage() {
        return mobData.getHealth() <= 0;
    }

    private void spawnMeat() {
        Point lastPosition = mobData.getPosition();
        int energy = mobData.getEnergy();
        resourceSpawner.spawnMeat(lastPosition, energy);
    }

    private void spawnCarrion() {
        Point lastPosition = mobData.getPosition();
        int energy = mobData.getHealth() * 5;
        resourceSpawner.spawnCarrion(lastPosition, energy);
    }

    public MobData getMobData() {
        return mobData;
    }

    public MobBehaviour getMobBehaviour() {
        return mobBehaviour;
    }
}
