package com.codecool.MobBehaviour;

import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.Board;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

import java.util.List;

public class ScavengerBehaviour implements MobBehaviour {

    private final MobFactory factory;
    private final MobData mobData;
    private final String SCAVENGER_MOB = "scavenger";
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
        }

        if (target == null) {
            stayInPlace();
        } else {
            moveTowardsTarget();
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
    }

    private void stayInPlace() {
        mobData.decreaseEnergy(1);
    }

    private void validateTarget() {
        Point previousTarget = target;
        target = null;
        Board board = mobData.getBoard();
        setTargetIfContainsFood(board, previousTarget);
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
        ComponentContainer container = board.getBoard().get(point);
        if (containerHasFood(container)) {
            target = point;
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
            factory.spawnMob(currentPosition, energyAfterReproduce, SCAVENGER_MOB);
            mobData.setEnergy(energyAfterReproduce);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }
    }
}
