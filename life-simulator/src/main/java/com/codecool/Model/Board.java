package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.Map;
import java.util.Random;

public class Board {

    private Map<Point, ComponentContainer> board;
    private int width;
    private int height;

    public Board(Map<Point, ComponentContainer> board, int width, int height) {
        this.board = board;
        this.width = width;
        this.height = height;
    }

    public void moveToPosition(MobData mobData, Point desiredPosition) {
        board.get(mobData.getPosition()).removeMob(mobData);
        mobData.setPosition(desiredPosition);
        board.get(desiredPosition).addMob(mobData);
    }

    public void spawnResource(Resource resource, Point point) {
        board.get(point).addResource(resource);
    }

    public boolean isPointAvailableForMob(Point point) {
        return !board.get(point).hasMobs();
    }

    public Point getRandomPoint() {
        Random random = new Random();
        int x = random.nextInt(getWidth());
        int y = random.nextInt(getHeight());
        return new Point(x, y);
    }

    public Map<Point, ComponentContainer> getBoard() {
        return board;
    }

    public void setBoard(Map<Point, ComponentContainer> board) {
        this.board = board;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
