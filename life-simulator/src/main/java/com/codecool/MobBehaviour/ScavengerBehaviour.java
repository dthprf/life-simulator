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

public class ScavengerBehaviour implements MobBehaviour {

    private final MobFactory factory;
    private final MobData mobData;
    private Point target;

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

        if (target == null) {
            stayInPlace();
        } else {
            moveTowardsTarget();
        }
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
