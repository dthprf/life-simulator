package com.codecool.Thread;

import com.codecool.Factory.ResourceSpawner;
import com.codecool.MobBehaviour.MobBehaviour;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

public class MobThread implements Runnable {
    MobData mobData;
    MobBehaviour mobBehaviour;
    ResourceSpawner resourceSpawner;

    public MobThread(MobData mobData, MobBehaviour mobBehaviour, ResourceSpawner resourceSpawner) {
        this.mobData = mobData;
        this.mobBehaviour = mobBehaviour;
        this.resourceSpawner = resourceSpawner;
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        while (!t.isInterrupted() && mobData.getHealth() > 0 && mobData.getEnergy() > 0) {
            try {
                Thread.sleep(10000 / mobData.getSpeed());
                mobBehaviour.update();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        spawnCarrion();
    }

    private void spawnCarrion() {
        if (mobData.getHealth() > 0) {
            Point lastPosition = mobData.getPosition();
            int energy = mobData.getHealth() * 5;
            resourceSpawner.spawnCarrion(lastPosition, energy);
        }
    }
}
