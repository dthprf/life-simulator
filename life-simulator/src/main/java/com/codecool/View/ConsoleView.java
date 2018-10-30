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
        int lastY = points.get(0).getY();
        System.out.println(boardSeparator(board));
        for (Point p : points) {
            if (p.getY() != lastY) {
                System.out.println();
                lastY = p.getY();
            }
            System.out.print(board.getBoard().get(p).asChar());
        }
        System.out.println();
    }

    private String boardSeparator(Board board) {
        return new String(new char[board.getWidth()]).replace("\0", "-");
    }
}
