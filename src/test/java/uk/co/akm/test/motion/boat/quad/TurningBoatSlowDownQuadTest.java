package uk.co.akm.test.motion.boat.quad;

import org.junit.Test;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.test.BoatSlowDownTest;

/**
 * Created by Thanos Mavroidis on 03/02/2018.
 */
public final class TurningBoatSlowDownQuadTest extends BoatSlowDownTest {
    private final double distanceLimitCloseness = 0.01;

    @Override
    protected double computeDistanceLimit(double k, double v0) {
        final double v0Abs = Math.abs(v0);

        return (1 + Math.log(v0Abs))/k;
    }

    @Override
    protected UpdatableState boatInstance(BoatConstants constants, double v) {
        return new TurningBoat(constants, 0, 0, v, 0, 0, 0);
    }

    @Test
    public void shouldComeToRest() {
        final double v0 = 10; // 36 km/h
        testSlowDown(v0);
    }

    @Test
    public void shouldComeToRestGoingBackwards() {
        final double v0 = -10; // -36 km/h
        testSlowDown(v0);
    }

    private void testSlowDown(double v0) {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double time = 600; // 10 min (enough to slow down)
        slowDownTest(constants, v0, time, distanceLimitCloseness);
    }
}
