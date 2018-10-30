package com.codecool.MobBehaviour;

import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HerbivoreBehaviour implements MobBehaviour{

    private final MobFactory factory;
    private final MobData mobData;
    private Point previousPoint;

    public HerbivoreBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {
        List<Point> reachZone = mobData.getBoard().adjacentPoints(mobData.getPosition(), 1);
        List<Point> sightZone = mobData.getBoard().adjacentPoints(mobData.getPosition(), 1);
    }

    private Point getDirection(List<Point> reachZone, List<Point> sightZone) {


    }

    private List<Point> getDangerPointsInSightZone(List<Point> sightZone) {
        List<Point> dangerPoints = sightZone.stream()
                .filter(p -> mobData.getBoard().getBoard().get(p).hasMobsOfType("predator"))
                .collect(Collectors.toList());

        return dangerPoints;
    }

    @Override
    public void reproduce() {
    }

}
