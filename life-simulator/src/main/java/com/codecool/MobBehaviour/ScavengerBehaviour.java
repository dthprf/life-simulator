package com.codecool.MobBehaviour;

import com.codecool.Factory.MobFactory;
import com.codecool.Model.Board;
import com.codecool.Model.MobData.MobData;

public class ScavengerBehaviour implements MobBehaviour {

    private final MobFactory factory;
    private final MobData mobData;
    private final Board board;

    public ScavengerBehaviour(MobFactory factory, MobData mobData, Board board) {
        this.factory = factory;
        this.mobData = mobData;
        this.board = board;
    }

    @Override
    public void update() {

    }

    @Override
    public void reproduce() {

    }
}
