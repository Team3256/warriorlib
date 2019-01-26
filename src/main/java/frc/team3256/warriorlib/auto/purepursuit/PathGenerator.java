package frc.team3256.warriorlib.auto.purepursuit;

import frc.team3256.warriorlib.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class PathGenerator {
    private double spacing, lookaheadDistance;
    private double a = 0, b = 0, tolerance = 0;
    private ArrayList<Vector> points = new ArrayList<>();

    public PathGenerator(double spacing, double lookaheadDistance) {
        this.spacing = spacing;
        this.lookaheadDistance = lookaheadDistance;
    }

    public void setSmoothingParameters(double a, double b, double tolerance) {
        this.a = a;
        this.b = b;
        this.tolerance = tolerance;
    }

    public void addPoint(Vector point) {
        points.add(point);
    }

    public void addPoints(Vector... points) {
        this.points.addAll(Arrays.asList(points));
    }

    public Path generatePath() {
        Path path = new Path(spacing);
        for (int i = 0; i < points.size() - 1; ++i)
            path.addSegment(points.get(i), points.get(i + 1));
        path.addLastPoint();
        if (tolerance != 0)
            path.smooth(a, b, tolerance);
        return path;
    }
}
