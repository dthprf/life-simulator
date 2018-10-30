package com.codecool.Comparator;

import com.codecool.Model.Point;

import java.util.Comparator;

public class PointComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        int y1 = o1.getY();
        int y2 = o2.getY();
        if (y1 != y2) {
            return y1 - y2;
        }
        int x1 = o1.getX();
        int x2 = o2.getX();
        return x1 - x2;
    }
}
