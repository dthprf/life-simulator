package com.codecool.Creator;

import com.codecool.Model.Board;
import com.codecool.Model.ComponentContainer;
import com.codecool.Model.MobData.MobData;
import com.codecool.Model.Point;
import com.codecool.Model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardCreator {

    public Board createBoard(int width, int height) {
        Map<Point, ComponentContainer> board = new ConcurrentHashMap<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point key = new Point(x, y);
                List<MobData> mobData = new ArrayList<>();
                List<Resource> resources = new ArrayList<>();
                ComponentContainer container = new ComponentContainer(mobData, resources);
                board.put(key, container);
            }
        }
        return new Board(board, width, height);
    }
}
