package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

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
        board.get(mobData.getPosition()).remove(mobData);
        mobData.setPosition(desiredPosition);
        board.get(desiredPosition).add(mobData);
    }

    public void spawnResource(Resource resource, Point point) {
        List<Object> location = board.get(point);
        location.add(resource);
        board.put(point, location);
    }
    
    public void spawnElement(Object object, Point point) {
        List<Object> elementsOnLocation = board.get(point);
        elementsOnLocation.add(object);
    }

    public boolean isPointAvailableForMob(Point point) {
        List<Object> occupants = board.get(point);
        return  occupants.isEmpty();
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
