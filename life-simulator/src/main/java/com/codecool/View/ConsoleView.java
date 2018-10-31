package com.codecool.View;

import com.codecool.Comparator.PointComparator;
import com.codecool.Model.Board;
import com.codecool.Model.Point;

import java.util.ArrayList;
import java.util.List;

public class ConsoleView implements Runnable {
    private final Board board;
    private final int interval;
    private List<Point> sortedBoardKeys;

    public ConsoleView(Board board, int interval) {
        this.board = board;
        this.interval = interval;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        sortedBoardKeys = new ArrayList<>(board.getBoard().keySet());
        sortedBoardKeys.sort(new PointComparator());
        while (!thread.isInterrupted()) {
            try {
                Thread.sleep(interval);
                clearScreen();
                printBoard();
            } catch (InterruptedException e) {
                // Stop display
            }
        }
    }

    private void printBoard() {
        System.out.println(stringifyBoard());
    }

    private String stringifyBoard() {
        StringBuilder builder = new StringBuilder("\n");
        builder.append(Thread.activeCount());
        builder.append(boardSeparator(board));
        builder.append("\n");
        int lastY = sortedBoardKeys.get(0).getY();
        for (Point p : sortedBoardKeys) {
            if (p.getY() != lastY) {
                builder.append("\n");
                lastY = p.getY();
            }
            builder.append(board.getBoard().get(p).asChar());
        }
        builder.append("\n");
        return builder.toString();
    }

    private String boardSeparator(Board board) {
        return new String(new char[board.getWidth()]).replace("\0", "-");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
