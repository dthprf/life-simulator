package com.codecool.MobBehaviour;

import com.codecool.Constant.MobTypes;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.PredatorAction;
import com.codecool.Model.Resource;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

public class PredatorBehaviour extends Mob implements MobBehaviour {
    private final MobFactory factory;
    private final MobData mobData;
    private final int VIEW_DISTANCE = 10;
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

            if (mobData.getEnergy() > 180) {
                reproduce();
                isMoveDone = true;
            }

            if (mobData.getEnergy() > 100) {
                stayInPosition();
                return;
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
    }


    @Override
    public void reproduce() {
        Point currentPosition = mobData.getPosition();
        int energyAfterReproduce = mobData.getEnergy() / 2;

        try {
            factory.spawnMob(currentPosition, energyAfterReproduce, MobTypes.PREDATOR_MOB);
            mobData.setEnergy(energyAfterReproduce);

        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }
    }

    private PriorityQueue<PredatorAction> queueEfficientActions() {
        List<Point> lineOfSight = mobData.getBoard().adjacentPoints(mobData.getPosition(), VIEW_DISTANCE);
        PriorityQueue<PredatorAction> actionsQueue = new PriorityQueue<>(3,
                (action1, action2) -> (action2.calculateEnergyIncome() - action1.calculateEnergyIncome()));

        for (int i = 0; i < VIEW_DISTANCE; i++) {
            List<Point> closestFields = getFieldsInRange(i, lineOfSight);

            for (Point point : closestFields) {
                ComponentContainer container = getComponent(point);
                if (container.hasMobsOfType(MobTypes.HERBIVORE_MOB) || container.hasMobsOfType(MobTypes.SCAVENGER_MOB)) {
                    actionsQueue.offer(proposeHunting(point, getComponent(point), i));
                }

                if (!container.getResources().isEmpty()) {
                    PredatorAction action = proposeFoodCollecting(point, getComponent(point), i);
                    if (action != null) {
                        actionsQueue.offer(action);
                    }
                }
            }
        }
        return actionsQueue;
    }

    private void makeUpdateWhenNoFood() {
        if (mobData.getEnergy() > 30) {
            moveTowardsTarget(chooseRandomDirection());
        } else {
            stayInPosition();
        }
    }

    private void moveTowardsTarget(Point target) {
        moveToPoint(mobData, target);
    }

    private Point chooseRandomDirection() {
        List<Point> availablePoints = mobData.getBoard().adjacentPoints(mobData.getPosition(), 1);
        availablePoints.remove(mobData.getPosition());
        return availablePoints.get(new Random().nextInt(availablePoints.size()));
    }

    private void stayInPosition() {
        mobData.decreaseEnergy(1);
    }

    private ComponentContainer getComponent(Point point) {
        return mobData.getBoard().getBoard().get(point);
    }

    private PredatorAction proposeHunting(Point point, ComponentContainer container, int distance) {
        MobData bestPray = container.getBestPrey(mobData);
        return new PredatorAction(bestPray, distance, point);
    }

    private PredatorAction proposeFoodCollecting(Point point, ComponentContainer container, int distance) {
        Resource bestFoodOption = container.getBestFood(this.mobData.getFoodList());
        if (bestFoodOption != null) {
            return new PredatorAction(bestFoodOption, distance, point);
        }
        return null;
    }

    private void proceedInstantAction(PredatorAction action) {
        ComponentContainer targetContainer = getComponent(action.getCoordinate());
        if (action.getActionType().equals(COLLECT_FOOD)) {
            isMoveDone = collectResource(getComponent(action.getCoordinate()));
        }

        if (action.getActionType().equals(ATTACK_MOB)) {
            MobData target = targetContainer.getBestPrey(mobData);
            isMoveDone = attackMob(target);

        }
    }

    private boolean attackMob(MobData pray) {
        if (pray != null) {
            pray.dealDamage(mobData.getDamage());
            mobData.decreaseEnergy(3);
            return true;
        }
        return false;
    }

    private boolean collectResource(ComponentContainer container) {
        for (String foodType : mobData.getFoodList()) {
            Resource resource = container.removeResourceOfType(foodType);
            if (resource != null) {
                mobData.increaseEnergy(resource.getEnergy());
                return true;
            }
        }
        return true;
    }

    private List<Point> getFieldsInRange(int range, List<Point> lineOfSight) {
        return lineOfSight.stream()
                .filter(point -> countDistance(point, mobData.getPosition()) == range).collect(Collectors.toList());
    }

    private int countDistance(Point target, Point point) {
        double distance = Math.hypot((target.getX() - point.getX()), (target.getY() - point.getY()));

        return (int) Math.round(distance);
    }
}
