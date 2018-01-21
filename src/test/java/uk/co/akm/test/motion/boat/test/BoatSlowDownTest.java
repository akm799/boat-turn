package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

/**
 * Created by Thanos Mavroidis on 21/01/2018.
 */
public abstract class BoatSlowDownTest {
    private final int nSteps = 1000000;
    private final double velocityLimitCloseness = 1.0E-250;

    protected abstract double computeDistanceLimit(double k, double v0);

    protected abstract UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double vx0, double vy0, double x0, double y0);

    protected final void slowDownTest(BoatConstants constants, double v0, double time, double distanceLimitCloseness) {
        final double k = (v0 >= 0 ? constants.kLon() : constants.kLonReverse());
        final double distanceLimit = computeDistanceLimit(k, v0);

        // Non-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = boatInstance(constants, 0, 0, v0, 0, 0, 0);

        // Boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        final double v = Math.abs(underTest.v());
        final double vx = Math.abs(underTest.vx());
        Assert.assertTrue(v >= 0);
        Assert.assertTrue(vx >= 0);
        Assert.assertTrue(v < velocityLimitCloseness);
        Assert.assertTrue(vx < velocityLimitCloseness);
        Assert.assertTrue(underTest.v() * v0 >= 0); // Check that velocity direction has not changed.
        Assert.assertTrue(underTest.vx() * v0 >= 0); // Check that velocity direction has not changed.

        // Boat almost reached the theoretical distance limit.
        final double x = Math.abs(underTest.x());
        Assert.assertTrue(x < distanceLimit);
        Assert.assertTrue(x > distanceLimit - distanceLimitCloseness);
        Assert.assertTrue(underTest.x() * v0 > 0); // Check that the final x-coordinate sign is consistent with the initial velocity sign.
    }
}
