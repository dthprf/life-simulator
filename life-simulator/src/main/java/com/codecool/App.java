package com.codecool;

import com.codecool.Constant.MobTypes;
import com.codecool.Creator.BoardCreator;
import com.codecool.Exception.UnrecognizedMobBreedException;
import com.codecool.Factory.MobFactory;
import com.codecool.Factory.ResourceSpawner;
import com.codecool.Model.Board;
import com.codecool.View.ConsoleView;

public class App {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 12;
    private static final int RESOURCE_INTERVAL = 1000;
    private static final int VIEW_UPDATE_INTERVAL = 1000;

    public static void main(String[] args)  {
        BoardCreator boardCreator = new BoardCreator();
        Board board = boardCreator.createBoard(WIDTH, HEIGHT);
        ResourceSpawner resourceSpawner = new ResourceSpawner(RESOURCE_INTERVAL, board);
        MobFactory mobFactory = new MobFactory(board, resourceSpawner);
        try {
            mobFactory.spawnMob(20, MobTypes.HERBIVORE_MOB);
            mobFactory.spawnMob(20, MobTypes.PREDATOR_MOB);
            mobFactory.spawnMob(20, MobTypes.SCAVENGER_MOB);
        } catch (UnrecognizedMobBreedException e) {
            e.printStackTrace();
        }

        resourceSpawner.start();
        ConsoleView view = new ConsoleView(board, VIEW_UPDATE_INTERVAL);
        Thread viewThread = new Thread(view);
        viewThread.start();
    }
}
