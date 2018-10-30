package com.codecool.MobBehaviour;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.PredatorAction;
import com.codecool.Model.Resource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

public class PredatorBehaviour implements MobBehaviour {
    private final MobFactory factory;
    private final MobData mobData;
    private final String PREDATOR_MOB = "predator";
    private final int VIEW_DISTANCE = 4;
    private final String COLLECT_FOOD = "collect";
    private final String ATTACK_MOB = "attack";
    private boolean isMoveDone = false;

    public PredatorBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {
        PriorityQueue<PredatorAction> actionsQueue = queueEfficientActions();

        while (!isMoveDone) {
            PredatorAction action = actionsQueue.poll();

            if (mobData.getEnergy() > 120) {
                reproduce();
                isMoveDone = true;
            }

            if (action == null) {
                makeUpdateWhenNoFood();
                isMoveDone = true;

            } else if (action.isActionInstant()) {
                proceedInstantAction(action);

            } else {
                moveTowardsTarget(action.getCoordinate());
                isMoveDone = true;
            }
        }
        isMoveDone = false;
        return;
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
