package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.ArrayList;
import java.util.List;
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

    public void spawnMob(MobData mob, Point point) {
        board.get(point).addMob(mob);
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

    public List<Point> adjacentPoints(Point center, int distance) {
        List<Point> result = new ArrayList<>();
        Point adjacent;
        for (int x = center.getX() - distance; x <= center.getX() + distance; x++) {
            for (int y = center.getY() - distance; y <= center.getY() + distance; y++) {
                adjacent = new Point(x, y);
                if (board.containsKey(adjacent)) {
                    result.add(adjacent);
                }
            }
        }
        return result;
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
