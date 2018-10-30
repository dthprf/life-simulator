package com.codecool.MobBehaviour;

import com.codecool.Constant.MobTypes;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.Board;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScavengerBehaviour implements MobBehaviour {

    private final MobFactory factory;
    private final MobData mobData;
    private Point target;
    private final int REQUIRED_ENERGY_TO_REPRODUCE = 120;

    public ScavengerBehaviour(MobFactory factory, MobData mobData) {
        this.factory = factory;
        this.mobData = mobData;
    }

    @Override
    public void update() {
        validateTarget();
        if (target == null) {
            updateTarget();
        } else if (target.equals(mobData.getPosition())) {
            eat();
            updateTarget();
        }

        if (this.mobData.getEnergy() >= REQUIRED_ENERGY_TO_REPRODUCE) {
            reproduce();
        }

        if (target == null) {
            List<Point> dangerPoints = predatorsNearby();
            if (!dangerPoints.isEmpty()) {
                handleDangerousSituation(dangerPoints);
            } else {
                stayInPlace();
            }
        } else {
            if (target.equals(mobData.getPosition())) {
                stayInPlace();
            } else {
                moveTowardsTarget();
            }
        }
    }

    private void handleDangerousSituation(List<Point> dangerPoints) {
        boolean attacked = attackWeakPredators(dangerPoints);
        if (!attacked) {
            runAway(dangerPoints);
        }
    }

    private boolean attackWeakPredators(List<Point> dangerPoints) {
        int myDamage = mobData.getDamage();
        boolean attacked = false;
        for(Point point : dangerPoints) {
            ComponentContainer container = mobData.getBoard().getBoard().get(point);
            for (MobData otherMob : container.getMobs()) {
                if (otherMob.getBreed().equalsIgnoreCase(this.mobData.getBreed())) {
                    continue;
                }
                if (otherMob.getHealth() <= myDamage) {
                    otherMob.dealDamage(myDamage);
                    attacked = true;
                    this.mobData.decreaseEnergy(2);
                }
            }
        }
        return attacked;
    }

    private List<Point> predatorsNearby() {
        Point currentPosition = mobData.getPosition();
        Board board = mobData.getBoard();
        int DANGER_ZONE = 1;
        List<Point> sightZone = board.adjacentPoints(currentPosition, DANGER_ZONE);

        return sightZone.stream()
                .filter(p -> this.mobData.getBoard().getBoard().get(p).hasMobsOfType("predator"))
                .collect(Collectors.toList());
    }

    private void eat() {
        Point position = mobData.getPosition();
        List<Resource> resources = mobData.getBoard().getBoard().get(position).getResources();
        if (resources.isEmpty()) {
            return;
        }
        List<String> foodList = Arrays.asList(mobData.getFoodList());
        Resource resource = resources.stream()
                .filter(r -> foodList.contains(r.getName()))
                .findFirst().orElse(null);
        collectResource(position, resource);
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

    private void moveTowardsTarget() {
        int nextX = mobData.getPosition().getX();
        int nextY = mobData.getPosition().getY();
        if (nextX < target.getX()) {
            nextX++;
        } else if (nextX > target.getX()) {
            nextX--;
        }

        if (nextY < target.getY()) {
            nextY++;
        } else if (nextY > target.getY()) {
            nextY--;
        }

        Point nextPosition = new Point(nextX, nextY);
        mobData.getBoard().moveToPosition(mobData, nextPosition);
        mobData.decreaseEnergy(2);
    }

    private void stayInPlace() {
        mobData.decreaseEnergy(1);
    }

    private void validateTarget() {
        Board board = mobData.getBoard();
        setTargetIfContainsFood(board, target);
    }


    private void updateTarget() {
        Point currentPosition = mobData.getPosition();
        Board board = mobData.getBoard();
        int SIGHT_RANGE = 5;
        List<Point> sightZone = board.adjacentPoints(currentPosition, SIGHT_RANGE);
        for (Point point : sightZone) {
            if (target != null) {
                break;
            }
            setTargetIfContainsFood(board, point);
        }
    }

    private void setTargetIfContainsFood(Board board, Point point) {
        if (point == null) {
            return;
        }
        ComponentContainer container = board.getBoard().get(point);
        if (containerHasFood(container)) {
            target = point;
        } else {
            target = null;
        }
    }

    private boolean containerHasFood(ComponentContainer container) {
        for (String food : mobData.getFoodList()) {
            if (container.hasResourceOfType(food)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reproduce() {
        Point currentPosition = mobData.getPosition();
        int energyAfterReproduce = mobData.getEnergy() / 2;
        try {
            factory.spawnMob(currentPosition, energyAfterReproduce, MobTypes.SCAVENGER_MOB);
            mobData.setEnergy(energyAfterReproduce);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }
    }
}
