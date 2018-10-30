package com.codecool.MobBehaviour;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

public class PredatorBehaviour implements MobBehaviour {
    private final MobFactory factory;
    private final MobData mobData;
    private final String PREDATOR_MOB = "predator";

    public PredatorBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {

    }

    @Override
    public void reproduce() {
        Point currentPosition = mobData.getPosition();
        int energyAfterReproduce = mobData.getEnergy() / 2;

        try {
            factory.spawnMob(currentPosition, energyAfterReproduce, PREDATOR_MOB);
            mobData.setEnergy(energyAfterReproduce);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }
    }
}
