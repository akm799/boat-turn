package uk.co.akm.test.motion.boat.path.helper.impl;

import uk.co.akm.test.motion.boat.path.helper.BoatPath;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatPathImpl implements BoatPath {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private int i;
    private final double[][] points;

    public BoatPathImpl(int nPoints) {
        points = new double[nPoints][2];
    }

    @Override
    public void addPoint(State point) {
        points[i][0] = point.x();
        points[i][1] = point.y();
        setLimits(points[i][0], points[i][1]);
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
}
