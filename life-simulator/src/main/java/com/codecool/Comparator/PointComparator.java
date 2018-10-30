package com.codecool.Comparator;

import com.codecool.Model.Point;

import java.util.Comparator;

public class PointComparator implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        int x1 = o1.getX();
        int x2 = o2.getX();
        if (x1 != x2) {
            return x1 - x2;
        }
        int y1 = o1.getY();
        int y2 = o2.getY();
        return y1 - y2;
    }
}
