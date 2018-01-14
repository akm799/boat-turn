package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.path.helper.dim.DimSpecs;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.Limits;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatPathImpl implements BoatPath, DimSpecs {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private int i;
    private final double[][] points;

    public BoatPathImpl(int nPoints) {
        points = new double[nPoints][N_DIM];
    }

    @Override
    public void addPoint(State point) {
        points[i][X_INDEX] = point.x();
        points[i][Y_INDEX] = point.y();
        setLimits(points[i][X_INDEX], points[i][Y_INDEX]);
        i++;
    }

    private void setLimits(double x, double y) {
        setLimitX(x);
        setLimitY(y);
    }

    private void setLimitX(double x) {
        if (x < xMin) {
            xMin = x;
        }

        if (x > xMax) {
            xMax = x;
        }
    }

    private void setLimitY(double y) {
        if (y < yMin) {
            yMin = y;
        }

        if (y > yMax) {
            yMax = y;
        }
    }

    @Override
    public void resetLimits(Limits other) {
        checkLimits(other);

        xMin = other.xMin();
        xMax = other.xMax();
        yMin = other.yMin();
        yMax = other.yMax();
    }

    private void checkLimits(Limits other) {
        if (xMin < other.xMin() || xMax > other.xMax() || yMin < other.yMin() || yMax > other.yMax()) {
            throw new IllegalArgumentException("New path limits exclude portion of the path.");
        }
    }

    @Override
    public double xMin() {
        return xMin;
    }

    @Override
    public double xMax() {
        return xMax;
    }

    @Override
    public double yMin() {
        return yMin;
    }

    @Override
    public double yMax() {
        return yMax;
    }

    @Override
    public int numberOfPoints() {
        return i;
    }

    @Override
    public double[][] pathPoints() {
        return points;
    }

    @Override
    public String toString() {
        return ("min=(" + xMin + ", " + yMin + ") max=(" + xMax + ", " + yMax + ")  " + i + " points.");
    }
}
