package com.codecool.View;

import com.codecool.Comparator.PointComparator;
import com.codecool.Model.Board;
import com.codecool.Model.Point;

import java.util.ArrayList;
import java.util.List;

public class ConsoleView {

    public void printBoard(Board board) {
        List<Point> points = new ArrayList<>(board.getBoard().keySet());
        points.sort(new PointComparator());
        int lastX = points.get(0).getX();
        System.out.println(boardSeparator(board));
        for (Point p : points) {
            if (p.getX() != lastX) {
                System.out.println();
                lastX = p.getX();
            }
            System.out.print(board.getBoard().get(p).asChar());
        }
        System.out.println();
    }

    private String boardSeparator(Board board) {
        return new String(new char[board.getHeight()]).replace("\0", "-");
    }
}
