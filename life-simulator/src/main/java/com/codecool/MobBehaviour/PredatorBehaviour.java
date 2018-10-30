package com.codecool.MobBehaviour;

import com.codecool.Factory.MobFactory;
import com.codecool.Model.MobData.MobData;

public class PredatorBehaviour implements MobBehaviour {
    private final MobFactory factory;
    private final MobData mobData;

    public PredatorBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {

    }

    @Override
    public void reproduce() {

    }
}
