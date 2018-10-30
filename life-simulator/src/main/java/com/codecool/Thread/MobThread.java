package com.codecool.Thread;

import com.codecool.MobBehaviour.MobBehaviour;
import com.codecool.Model.MobData.MobData;

public class MobThread implements Runnable {
    MobData mobData;
    MobBehaviour mobBehaviour;

    public MobThread(MobData mobData, MobBehaviour mobBehaviour) {
        this.mobData = mobData;
        this.mobBehaviour = mobBehaviour;
    }

    @Override
    public void run() {
        Thread t = Thread.currentThread();
        while(!t.isInterrupted()) {
            try {
                Thread.sleep(10000/mobData.getSpeed());
                mobBehaviour.update();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
