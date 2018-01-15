package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.path.helper.dim.DimSpecs;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.Limits;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 15/01/2018.
 */
public abstract class AbstractBoatPath implements BoatPath, DimSpecs {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private int i;
    private final double[][] points;

    private final double[] point = new double[N_DIM];

    public AbstractBoatPath(int nPoints) {
        points = new double[nPoints][N_DIM];
    }

    @Override
    public final int capacity() {
        return points.length;
    }

    @Override
    public final void addPoint(State state) {
        copyStateToPoint(state, point);
        addPoint(point[X_INDEX], point[Y_INDEX]);
    }

    protected abstract void copyStateToPoint(State state, double[] point);

    private void addPoint(double x, double y) {
        points[i][X_INDEX] = x;
        points[i][Y_INDEX] = y;
        setLimits(x, y);
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
    public final void resetLimits(Limits other) {
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
    public final double xMin() {
        return xMin;
    }

    @Override
    public final double xMax() {
        return xMax;
    }

    @Override
    public final double yMin() {
        return yMin;
    }

    @Override
    public final double yMax() {
        return yMax;
    }

    @Override
    public final int numberOfPoints() {
        return i;
    }

    @Override
    public final double[][] pathPoints() {
        return points;
    }

    @Override
    public String toString() {
        return ("min=(" + xMin + ", " + yMin + ") max=(" + xMax + ", " + yMax + ")  " + i + " points.");
    }
}
