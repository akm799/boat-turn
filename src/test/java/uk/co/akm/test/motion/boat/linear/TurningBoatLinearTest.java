package uk.co.akm.test.motion.boat.linear;

import org.junit.Assert;
import org.junit.Test;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public class TurningBoatLinearTest {
    private final int nSteps = 1000000;

    @Test
    public void shouldComeToRest() {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 600; // 10 min (enough to slow down)

        final double distanceLimit = v0/kLon;
        final double distanceLimitCloseness = 0.007;
        final double velocityLimitCloseness = 1.0E-250;

        // Non-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = new TurningBoat(constants, 0, 0, v0, 0, 0, 0);

        // Boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        Assert.assertFalse(underTest.v() < 0);
        Assert.assertFalse(underTest.vx() < 0);
        Assert.assertTrue(underTest.v() < velocityLimitCloseness);
        Assert.assertTrue(underTest.vx() < velocityLimitCloseness);

        // Boat almost reached the theoretical distance limit.
        Assert.assertTrue(underTest.x() > 0);
        Assert.assertTrue(underTest.x() < distanceLimit);
        Assert.assertTrue(underTest.x() > distanceLimit - distanceLimitCloseness);
    }

    @Test
    public void shouldComeToRestGoingBackwards() {
        final BoatConstants constants = new BoatConstantsImpl(1, 50, 10);
        final double kLonReverse = constants.kLonReverse();

        final double v0 = 10; // 36 km/h
        final double time = 600; // 10 min (enough to slow down)

        final double distanceLimit = v0/kLonReverse;
        final double distanceLimitCloseness = 0.007;
        final double velocityLimitCloseness = 1.0E-250;

        // Non-turning boat setting of from the origin with an initial speed v0 along the negative x-axis direction.
        final UpdatableState underTest = new TurningBoat(constants, 0, 0, -v0, 0, 0, 0);

        // Boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        Assert.assertFalse(underTest.v() < 0);
        Assert.assertFalse(underTest.vx() > 0);
        Assert.assertTrue(underTest.v() < velocityLimitCloseness);
        Assert.assertTrue(underTest.vx() > -velocityLimitCloseness);

        // Boat almost reached the theoretical distance limit.
        Assert.assertTrue(underTest.x() < 0);
        Assert.assertTrue(underTest.x() > -distanceLimit);
        Assert.assertTrue(underTest.x() < -distanceLimit + distanceLimitCloseness);
    }
}
