package uk.co.akm.test.motion.boat.quad;

import org.junit.Test;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.test.TurningBoatPathTest;


/**
 * Created by Thanos Mavroidis on 20/01/2018.
 */
public final class TurningBoatPathQuadTest extends TurningBoatPathTest {

    protected String imageSubFolder() {
        return "quad";
    }

    protected UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double vx0, double vy0, double x0, double y0) {
        return new TurningBoat(constants, omgHdn0, hdn0, vx0, vy0, x0, y0);
    }

    @Test
    public void shouldComeToRestWhileTurningWhileTurningSlowly() {
        comeToRestWhileTurningWhileTurningSlowlyTest();
    }

    @Test
    public void shouldProducePositionPathWhileTurningSlowly() {
        producePositionPathWhileTurningSlowlyTest();
    }

    @Test
    public void shouldProduceAnglesPathWhileTurningSlowly() {
        produceAnglesPathWhileTurningSlowlyTest();
    }

    @Test
    public void shouldProduceMultiplePositionPathsWithOmegaVariation() {
        produceMultiplePositionPathsWithOmegaVariationTest();
    }

    @Test
    public void shouldProduceMultipleAnglesPathsWithOmegaVariation() {
        produceMultipleAnglesPathsWithOmegaVariationTest();
    }

    @Test
    public void shouldProduceMultiplePositionPathsWithKRatioVariation() {
        produceMultiplePositionPathsWithKRatioVariationTest();
    }

    @Test
    public void shouldProduceMultipleAnglesPathsWithKRatioVariation() {
        produceMultipleAnglesPathsWithKRatioVariationTest();
    }
}
