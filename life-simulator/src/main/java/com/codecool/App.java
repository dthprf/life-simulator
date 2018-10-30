package com.codecool;

import com.codecool.Constant.MobTypes;
import com.codecool.Creator.BoardCreator;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Factory.ResourceSpawner;
import com.codecool.Model.Board;

public class App {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int RESOURCE_INTERVAL = 1000;

    public static void main(String[] args)  {
        BoardCreator boardCreator = new BoardCreator();
        Board board = boardCreator.createBoard(WIDTH, HEIGHT);
        MobFactory mobFactory = new MobFactory(board);
        try {
            mobFactory.spawnMob(2, MobTypes.HERBIVORE_MOB);
            mobFactory.spawnMob(2, MobTypes.PREDATOR_MOB);
            mobFactory.spawnMob(2, MobTypes.SCAVENGER_MOB);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }

        Thread resourceSpawner = new ResourceSpawner(RESOURCE_INTERVAL, board);
        resourceSpawner.start();
    }
}
