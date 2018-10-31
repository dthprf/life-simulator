package com.codecool.MobBehaviour;

import com.codecool.Constant.MobTypes;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.Board;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScavengerBehaviour extends Mob implements MobBehaviour {

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
        System.out.println(mobData.getEnergy());
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

        List<Point> dangerPoints = predatorsNearby();
        if (!dangerPoints.isEmpty()) {
            handleDangerousSituation(dangerPoints);
        } else {
            stayInPlace();
        }

        if (target != null) {
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

    private void runAway(List<Point> dangerPoints) {
        Point pointOfHighestThreat = getBiggestThreat(dangerPoints);
        Point nextPosition = getEscapePoint(pointOfHighestThreat);
        mobData.getBoard().moveToPosition(mobData, nextPosition);
        mobData.decreaseEnergy(2);
    }

    private Point getEscapePoint(Point pointOfHighestThreat) {
        int threatX = pointOfHighestThreat.getX();
        int threatY = pointOfHighestThreat.getY();
        int nextX = this.mobData.getPosition().getX();
        int nextY = this.mobData.getPosition().getY();
        Board board = this.mobData.getBoard();
        if (threatX > nextX) {
            if (nextX - 1 > 0) {
                nextX--;
            }
        } else {
            if (nextX + 1 < board.getWidth()) {
                nextX++;
            }
        }

        if (threatY > nextY) {
            if (nextY - 1 > 0) {
                nextY--;
            }
        } else {
            if (nextY + 1 < board.getHeight()) {
                nextY++;
            }
        }

        return new Point(nextX, nextY);
    }

    private Point getBiggestThreat(List<Point> dangerPoints) {
        Integer lowestEnergy = null;
        Point result = null;
        for (Point point : dangerPoints) {
            ComponentContainer container = mobData.getBoard().getBoard().get(point);
            int predatorEnergy = container.getMobs().stream()
                    .filter(mob -> mob.getBreed().equalsIgnoreCase(MobTypes.PREDATOR_MOB))
                    .mapToInt(MobData::getEnergy)
                    .min().orElse(200);
            if (lowestEnergy == null || lowestEnergy > predatorEnergy) {
                lowestEnergy = predatorEnergy;
                result = point;
            }
        }
        return result;
    }

    private boolean attackWeakPredators(List<Point> dangerPoints) {
        int myDamage = mobData.getDamage();
        boolean attacked = false;
        for (Point point : dangerPoints) {
            ComponentContainer container = mobData.getBoard().getBoard().get(point);
            for (MobData otherMob : container.getMobs()) {
                if (otherMob.getBreed().equalsIgnoreCase(this.mobData.getBreed())) {
                    continue;
                }
                if (otherMob.getHealth() <= myDamage) {
                    otherMob.dealDamage(myDamage);
                    System.out.println("ATTACK MOB");
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

    private void moveTowardsTarget() {
        moveToPoint(mobData, target);
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
        int SIGHT_RANGE = 10;
        List<Point> sightZone = board.adjacentPoints(currentPosition, SIGHT_RANGE);
        Collections.shuffle(sightZone);
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
