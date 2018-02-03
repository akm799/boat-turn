package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.Rotation;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatOmegaPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatVLatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatVLonPath;
import uk.co.akm.test.motion.boat.phys.UpdatableState;


/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public abstract class RotatingBoatPathTest extends BaseBoatPathTest {
    private final String imageFileVLonPath = "boat-v-lon-path.png";
    private final String imageFileVLatPath = "boat-v-lat-path.png";
    private final String imageFileOmegaPath = "boat-omega-path.png";
    private final String imageFileOmegaNegativePath = "boat-omega-negative-path.png";

    protected abstract BoatConstants boatConstants(double kLon, double kLatOverKLon);

    protected abstract UpdatableState boatInstance(BoatConstants constants, Rotation rotation, double omgHdn0, double hdn0, double v0);

    protected final void rotationOmegaTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final BoatPathFactory factory = (int nPoints) -> new BoatOmegaPath(nPoints);
        rotationTest("omega", Rotation.LEFT, kLon, kLatOverKLon, factory, time, width, height, imageFileOmegaPath);
    }

    protected final void rotationOmegaNegativeTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final BoatPathFactory factory = (int nPoints) -> new BoatOmegaPath(nPoints);
        rotationTest("omega-negative", Rotation.RIGHT, kLon, kLatOverKLon, factory, time, width, height, imageFileOmegaNegativePath);
    }

    protected final void rotationVLonTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final BoatPathFactory factory = (int nPoints) -> new BoatVLonPath(nPoints);
        rotationTest("vLon", Rotation.LEFT, kLon, kLatOverKLon, factory, time, width, height, imageFileVLonPath);
    }

    protected final void rotationVLatTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final BoatPathFactory factory = (int nPoints) -> new BoatVLatPath(nPoints);
        rotationTest("vLat", Rotation.LEFT, kLon, kLatOverKLon, factory, time, width, height, imageFileVLatPath);
    }

    private final void rotationTest(String name, Rotation rotation, double kLon, double kLatOverKLon, BoatPathFactory factory, double time, int width, int height, String imageFileName) {
        final int nPathPoints = 10000;

        final BoatPathUpdater pathUpdater = new BoatPathUpdater(factory.instance(nPathPoints), nSteps);

        final BoatConstants constants = boatConstants(kLon, kLatOverKLon);
        final UpdatableState underTest = boatInstance(constants, rotation,0, 0, constants.getRudderData().v);

        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, false, path);

        writeToImageFile(name, path.toString(), pixels, imageFileName);
    }
}
