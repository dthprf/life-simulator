package com.codecool.MobBehaviour;

import com.codecool.Constant.MobTypes;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HerbivoreBehaviour implements MobBehaviour{

    private final MobFactory factory;
    private final MobData mobData;
    private Point target;

    public HerbivoreBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {
        List<Point> reachZone = mobData.getBoard().adjacentPoints(mobData.getPosition(), 1);

        validateTarget();
        if(target != null && reachZone.contains(this.target)) {
            eat(reachZone);
        }
        goToTarget();
    }

    private void validateTarget() {
        if(this.target != null) {
            updateTarget();
        } else {
            setTarget(mobData.getPosition());
        }
    }

    private void updateTarget() {
        if (!isTargetContainsFood(this.target)) {
            this.target = null;
        }
    }

    private void setTarget(Point point) {
        List<Point> reachZone = mobData.getBoard().adjacentPoints(point, 1);

        this.target = searchForTargetInBetweenReachAndSight(point, reachZone);

        if(this.target == null) {
            List<Point> reachPlusOne = mobData.getBoard().adjacentPoints(point, 2);
            this.target = searchForTargetOnSightArea(point, reachPlusOne);
        }

        if(this.target == null) {
            setRandomTarget();
        }
    }

    private void setRandomTarget() {
        Random random = new Random();

        int nextX;
        int nextY;

        nextX = random.nextInt(3) - 1;
        nextY = random.nextInt(3) - 1;

        this.target = new Point(nextX, nextY);
    }

    private Point searchForTargetOnSightArea(Point point, List<Point> reachPlusOne) {
        List<Point> sightArea = mobData.getBoard().adjacentPoints(point, 3);
        sightArea.removeIf(p -> reachPlusOne.contains(p));

        sightArea = getResourcePointsInZone(sightArea);

        for(Point p: sightArea) {
            List<Point> surrounding = mobData.getBoard().adjacentPoints(p, 1);
            if(isSurroundingSafe(surrounding)) {
                return p;
            }
        }
        return null;
    }

    private Point searchForTargetInBetweenReachAndSight(Point point, List<Point> reachZone) {
        List<Point> betweenSightAndReach = mobData.getBoard().adjacentPoints(point, 2);
        betweenSightAndReach.removeIf(p -> reachZone.contains(p));

        betweenSightAndReach = getResourcePointsInZone(betweenSightAndReach);


        for(Point p: betweenSightAndReach) {
            List<Point> surrounding = mobData.getBoard().adjacentPoints(p, 1);
            if(isSurroundingSafe(surrounding)) {
                return p;
            }
        }
        return null;
    }

    private void goToTarget() {

        if(this.target.equals(mobData.getPosition())) {
            stayInPlace();
        } else {
            int nextX = mobData.getPosition().getX();
            int nextY = mobData.getPosition().getY();
            if (nextX < this.target.getX()) {
                nextX++;
            } else if (nextX > this.target.getX()) {
                nextX--;
            }

            if (nextY < this.target.getY()) {
                nextY++;
            } else if (nextY > this.target.getY()) {
                nextY--;
            }

            Point nextPosition = new Point(nextX, nextY);
            mobData.getBoard().moveToPosition(mobData, nextPosition);
            mobData.decreaseEnergy(2);
        }
    }

    private void stayInPlace() {
        mobData.decreaseEnergy(1);
    }

    private boolean isTargetContainsFood(Point point) {
        ComponentContainer targetContainer = mobData.getBoard().getBoard().get(point);

        for(Resource resource: targetContainer.getResources()) {
            if(resource.getName().equals("water") || resource.getName().equals("herb")) {
                return true;
            }
        }
        return false;
    }

    private void eat(List<Point> reachZone) {

        for(Point point: reachZone) {
            if(mobData.getBoard().getBoard().get(point).hasResourceOfType("water") ||
                  mobData.getBoard().getBoard().get(point).hasResourceOfType("herb")) {
                List<Resource> resources = mobData.getBoard().getBoard().get(point).getResources();
                Resource resource = resources.stream()
                        .filter(r -> r.getName().equals("water") || r.getName().equals("herb"))
                        .findFirst().get();
                collectResource(point, resource);
            }
        }
    }

    private boolean isSurroundingSafe(List<Point> surrounding) {
        surrounding.remove(this.mobData.getPosition());
        for(Point point: surrounding) {
            if(this.mobData.getBoard().getBoard().get(point)
                    .hasMobsOfType("predator")) {
                return false;
            }
        }
        return true;
    }

    private List<Point> getResourcePointsInZone(List<Point> zone) {
        List<Point> resourcePoints = zone.stream()
                .filter(p -> (this.mobData.getBoard().getBoard().get(p).hasResourceOfType("water")
                || this.mobData.getBoard().getBoard().get(p).hasResourceOfType("herb")))
                .collect(Collectors.toList());

        return resourcePoints;
    }
//
//    private List<Point> getDangerPointsInZone(List<Point> zone) {
//        List<Point> dangerPoints = zone.stream()
//                .filter(p -> this.mobData.getBoard().getBoard().get(p).hasMobsOfType("predator"))
//                .collect(Collectors.toList());
//
//        return dangerPoints;
//    }

    private void collectResource(Point point, Resource resource) {
        if (resource == null) {
            return;
        }
        boolean foundFood = this.mobData.getBoard().getBoard().get(point).removeResource(resource);
        if (foundFood) {
            this.mobData.increaseEnergy(resource.getEnergy());
        }
    }

    @Override
    public void reproduce() {
        Point currentPosition = mobData.getPosition();
        int energyAfterReproduce = mobData.getEnergy() / 2;
        try {
            factory.spawnMob(currentPosition, energyAfterReproduce, MobTypes.HERBIVORE_MOB);
            mobData.setEnergy(energyAfterReproduce);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }
    }

}
