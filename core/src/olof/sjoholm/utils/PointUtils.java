package olof.sjoholm.utils;

import java.awt.Point;

public class PointUtils {

    private PointUtils() {

    }

    public static Point add(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }
}
