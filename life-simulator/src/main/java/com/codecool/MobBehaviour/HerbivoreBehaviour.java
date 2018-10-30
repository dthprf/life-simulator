package com.codecool.MobBehaviour;

import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

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

    private void doAction(List<Point> reachZone, List<Point> sightZone) {
        List<Point> dangerPoints = getDangerPointsInSightZone(sightZone);
        for(Point point: reachZone) {
            if(dangerPoints.contains(point)) {
                setDirection();
            } else if(mobData.getBoard().getBoard().get(point).hasResourceOfType("water") ||
                      mobData.getBoard().getBoard().get(point).hasResourceOfType("herb")) {
                List<Resource> resources = mobData.getBoard().getBoard().get(point).getResources();
                Resource resource = resources.stream()
                        .filter(r -> r.getName().equals("water") || r.getName().equals("herb"))
                        .findFirst().get();
                collectResource(point, resource);
                setDirection();
            } else {
                setDirection();
            }
        }
    }

    private void setDirection() {

    }

    private List<Point> getDangerPointsInSightZone(List<Point> sightZone) {
        List<Point> dangerPoints = sightZone.stream()
                .filter(p -> this.mobData.getBoard().getBoard().get(p).hasMobsOfType("predator"))
                .collect(Collectors.toList());

        return dangerPoints;
    }

    private void collectResource(Point point, Resource resource) {
        this.mobData.getBoard().getBoard().get(point).removeResource(resource);
        this.mobData.setEnergy(this.mobData.getEnergy() + resource.getEnergy());
    }

    @Override
    public void reproduce() {
    }

}
