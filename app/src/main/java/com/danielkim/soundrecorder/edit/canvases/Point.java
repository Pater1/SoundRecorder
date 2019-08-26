package com.danielkim.soundrecorder.edit.canvases;

public class Point {

    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceSquaredTo(Point p2) {
        return Math.pow(this.x - p2.x, 2) + Math.pow(this.y - p2.y, 2);
    }

    double distanceTo(Point p2) {
        return Math.sqrt(distanceSquaredTo(p2));
    }

    Point add(Point p2) {
        return new Point(this.x + p2.x, this.y + p2.y);
    }
}