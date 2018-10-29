package com.codecool.Model;

import java.util.List;
import java.util.Map;

public class Board {

    private Map<Point, List<Object>> board;
    private int width;
    private int height;

    public Board(Map<Point, List<Object>> board, int width, int height) {
        this.board = board;
        this.width = width;
        this.height = height;
    }

    public void moveToPosition(MobData mobData, Point desiredPosition) {
        mobData.setPoint(desiredPosition);
    }

    public void spawnResource(Resource resource, Point point) {
        List<Object> location = board.get(point);
        location.add(resource);
        board.put(point, location);
    }

    public Map getBoard() {
        return board;
    }

    public void setBoard(Map board) {
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
