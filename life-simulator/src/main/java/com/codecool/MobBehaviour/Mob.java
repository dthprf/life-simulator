package com.codecool.MobBehaviour;

import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;

abstract class Mob {

    void moveToPoint(MobData mobData, Point target) {
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
}
