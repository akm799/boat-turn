package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatOmegaPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.phys.UpdatableState;


/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public abstract class RotatingBoatPathTest extends BaseBoatPathTest {
    private final String imageFileOmegaPath = "boat-omega-path.png";

    protected abstract BoatConstants boatConstants(double kLon, double kLatOverKLon);

    protected abstract UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double v0);

    protected final void rotationTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final int nPathPoints = 10000;

        final BoatPathUpdater pathUpdater = new BoatPathUpdater(new BoatOmegaPath(nPathPoints), nSteps);

        final BoatConstants constants = boatConstants(kLon, kLatOverKLon);
        final UpdatableState underTest = boatInstance(constants, 0, 0, constants.getRudderData().v);

        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, false, path);

        writeToImageFile("omega", path.toString(), pixels, imageFileOmegaPath);
    }
}
