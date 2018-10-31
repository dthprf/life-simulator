package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

public class PredatorAction {
    private static final int ENERGY_LOOSE_RISK_RATIO = 50;
    private static final String COLLECT_FOOD = "collect";
    private static final String ATTACK_MOB = "attack";
    private String actionType;
    private int distance;
    private int energyIncome;
    private Point coordinate;

    public PredatorAction(Resource resource, int distance, Point coordinate) {
        this.actionType = COLLECT_FOOD;
        this.distance = distance;
        this.energyIncome = resource == null ? 0 : resource.getEnergy();
        this.coordinate = coordinate;
    }

    public PredatorAction(MobData mob, int distance, Point coordinate) {
        this.actionType = ATTACK_MOB;
        this.distance = distance;
        this.energyIncome = mob == null ? 0 : mob.getEnergy();
        this.coordinate = coordinate;
    }

    public int calculateEnergyIncome() {
        return energyIncome - (distance * ENERGY_LOOSE_RISK_RATIO);
    }

    public String getActionType() {
        return actionType;
    }

    public int getDistance() {
        return distance;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public int getEnergyIncome() {
        return energyIncome;
    }

    public boolean isActionInstant() {
        return distance <= 1;
    }
}
