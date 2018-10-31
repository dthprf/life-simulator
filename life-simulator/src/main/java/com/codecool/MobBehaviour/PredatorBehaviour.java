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

    private PriorityQueue<PredatorAction> queueEfficientActions() {
        List<Point> lineOfSight = mobData.getBoard().adjacentPoints(mobData.getPosition(), VIEW_DISTANCE);
        PriorityQueue<PredatorAction> actionsQueue = new PriorityQueue<>(3,
                (action1, action2) -> (action2.calculateEnergyIncome() - action1.calculateEnergyIncome()));

        for (int i = 0; i < VIEW_DISTANCE; i++) {
            List<Point> closestFields = getFieldsInRange(i, lineOfSight);

            for (Point point : closestFields) {
                if (!getComponent(point).getMobs().stream().filter(mob -> !mobData.getBreed().equals(mob.getBreed())).collect(Collectors.toList()).isEmpty()) {
                    actionsQueue.offer(proposeHunting(point, getComponent(point), i));
                }

                if (!getComponent(point).getResources().isEmpty()) {
                    actionsQueue.offer(proposeFoodCollecting(point, getComponent(point), i));
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
        MobData bestPray = chooseBestPray(container.getMobs());
        return new PredatorAction(bestPray, distance, point);
    }

    private MobData chooseBestPray(List<MobData> allMobs) {
        List<MobData> availableMobs = allMobs.stream()
                .filter(mob -> !mobData.getBreed().equals(mob.getBreed())).collect(Collectors.toList());
        return Collections.max(availableMobs, Comparator.comparing(c -> c.getEnergy()));
    }

    private PredatorAction proposeFoodCollecting(Point point, ComponentContainer container, int distance) {
        Resource bestFoodOption = chooseBestFood(container.getResources());
        return new PredatorAction(bestFoodOption, distance, point);
    }


    private Resource chooseBestFood(List<Resource> availableFood) {
        return Collections.max(availableFood, Comparator.comparing(c -> c.getEnergy()));
    }

    private void proceedInstantAction(PredatorAction action) {
        ComponentContainer targetContainer = getComponent(action.getCoordinate());

        if (action.getActionType().equals(ATTACK_MOB)) {
            MobData target = chooseBestPray(targetContainer.getMobs());
            isMoveDone = attackMob(target);

        } else if (action.getActionType().equals(COLLECT_FOOD)) {
            Resource target = chooseBestFood(targetContainer.getResources());
            isMoveDone = collectResource(action.getCoordinate(), target);
        }
    }

    private boolean attackMob(MobData pray) {
        if (pray != null) {
            pray.dealDamage(mobData.getDamage());
            return true;
        }
        return false;
    }

    private boolean collectResource(Point point, Resource resource) {
        if (resource != null) {
            this.mobData.getBoard().getBoard().get(point).removeResource(resource);
            this.mobData.increaseEnergy(resource.getEnergy());
            return true;
        }
        return false;
    }

    private List<Resource> getValidFood(List<Resource> resources) {
        List<String> foodList = Arrays.asList(mobData.getFoodList());
        return resources.stream()
                .filter(r -> foodList.contains(r.getName()))
                .collect(Collectors.toList());
    }


}
