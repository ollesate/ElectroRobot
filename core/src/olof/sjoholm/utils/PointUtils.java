package olof.sjoholm.utils;

import java.awt.Point;

public class PointUtils {

    private PointUtils() {

    }

    public static int distance(Point a, Point b) {
        return Math.abs(b.x - a.x + b.y - a.y);
    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
}
