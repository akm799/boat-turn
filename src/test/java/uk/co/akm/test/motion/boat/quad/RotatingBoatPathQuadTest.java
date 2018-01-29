package uk.co.akm.test.motion.boat.quad;

import org.junit.Test;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.RudderData;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.test.RotatingBoatPathTest;

/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public final class RotatingBoatPathQuadTest extends RotatingBoatPathTest {
    private final RudderData rudderData = new RudderData(4, 1.5, 3*Math.PI/8, 2.5, 1);

    @Override
    protected String imageSubFolder() {
        return "quad/rotation";
    }

    @Override
    protected BoatConstants boatConstants(double kLon, double kLatOverKLon) {
        return new BoatConstantsImpl(kLon, kLatOverKLon, 10, rudderData);
    }

    @Override
    protected UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double v0) {
        return new RotatingBoat(constants, omgHdn0, hdn0, v0);
    }

    @Test
    public void shouldProduceOmegaPath() {
        rotationOmegaTest(1, 5,0.3, 600, 600);
    }

    @Test
    public void shouldProduceVLonPath() {
        rotationVLonTest(1, 5,10, 600, 600);
    }

    @Test
    public void shouldProduceVLatPath() {
        rotationVLatTest(1, 5,10, 600, 600);
    }
}
