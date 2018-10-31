package com.codecool.MobBehaviour;

import com.codecool.Constant.MobTypes;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class HerbivoreBehaviour implements MobBehaviour{

    private final MobFactory factory;
    private final MobData mobData;
    private final int SIGHT_DISTANCE = 7;
    private final int REQUIRED_ENERGY_TO_REPRODUCE = 120;
    private Point target;

    public HerbivoreBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {
        List<Point> reachZone = mobData.getBoard().adjacentPoints(mobData.getPosition(), 1);

        validateTarget();
        if(target != null && isTargetContainsFood(this.target)) {
            eat(reachZone);
            setTarget(mobData.getPosition());
        }
        if (this.mobData.getEnergy() >= REQUIRED_ENERGY_TO_REPRODUCE) {
            reproduce();
        }
        goToTarget();
    }

    private void validateTarget() {
        if(this.target != null) {
            updateTarget();
        }

        if(this.target == null) {
            setTarget(mobData.getPosition());
        }
    }

    private void updateTarget() {
        if (!isTargetContainsFood(this.target)) {
            this.target = null;
        }
    }

    private void setTarget(Point point) {
        this.target = searchForTargetInDistanceArea(point, SIGHT_DISTANCE);

        if(this.target == null) {
            setRandomTarget(point);
        }
    }

    private void setRandomTarget(Point point) {
        List<Point> reachZone = mobData.getBoard().adjacentPoints(point, 1);
        Random random = new Random();

        int randomIndex = random.nextInt(reachZone.size());

        this.target = reachZone.get(randomIndex);
    }

    private Point searchForTargetInDistanceArea(Point point, int distance) {
        for(int i = 1; i <= distance; i++) {

            List<Point> distanceArea = mobData.getBoard().adjacentPoints(point, i);

            if(i < 2) {
                List<Point> insideDistanceZone = mobData.getBoard().adjacentPoints(point, i - 1);
                distanceArea.removeIf(p -> insideDistanceZone.contains(p));
            }

            distanceArea = getResourcePointsInZone(distanceArea);
            Point target = getTarget(distanceArea);
            if(target != null) {
                return target;
            }
        }
        return null;
    }

    private Point getTarget(List<Point> distanceArea) {
        for(Point p: distanceArea) {
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
            eat(point);
        }
    }

    private void eat(Point position) {
        ComponentContainer container = mobData.getBoard().getBoard().get(position);
        collectResource(container);
    }

    private void collectResource(ComponentContainer container) {
        for (String foodType : mobData.getFoodList()) {
            Resource resource = container.removeResourceOfType(foodType);
            if (resource != null) {
                mobData.increaseEnergy(resource.getEnergy());
                break;
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
        return zone.stream()
                .filter(p -> (this.mobData.getBoard().getBoard().get(p).hasResourceOfType("water")
                || this.mobData.getBoard().getBoard().get(p).hasResourceOfType("herb")))
                .collect(Collectors.toList());
    }

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
