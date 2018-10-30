package com.codecool.MobBehaviour;

import com.codecool.Factory.MobFactory;
import com.codecool.Model.MobData.MobData;

public class ScavengerBehaviour implements MobBehaviour {

    private final MobFactory factory;
    private final MobData mobData;

    public ScavengerBehaviour(MobFactory factory, MobData mobData) {
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
