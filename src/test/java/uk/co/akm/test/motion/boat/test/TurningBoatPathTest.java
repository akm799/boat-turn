package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.math.MathConstants;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.Limits;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatAnglesPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPositionPath;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;


/**
 * Created by Thanos Mavroidis on 20/01/2018.
 */
public abstract class TurningBoatPathTest extends BaseBoatPathTest {
    private final String imageFileAnglesPath = "boat-angles-path.png";
    private final String imageFilePositionPath = "boat-position-path.png";
    private final String imageFileAnglesPathMultipleOmg = "boat-angles-path-omg-comparison.png";
    private final String imageFilePositionPathMultipleOmg = "boat-position-path-omg-comparison.png";
    private final String imageFileAnglesPathMultipleKRatio = "boat-angles-path-k-ratio-comparison.png";
    private final String imageFilePositionPathMultipleKRatio = "boat-position-path-k-ratio-comparison.png";

    protected abstract UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double vx0, double vy0, double x0, double y0);

    protected final void comeToRestWhileTurningWhileTurningSlowlyTest() {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)
        final double omg = MathConstants.PI_OVER_TWO/time;

        final double velocityLimitCloseness = 1.0E-25;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = boatInstance(constants, omg, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        Assert.assertFalse(underTest.v() < 0);
        Assert.assertTrue(underTest.v() < velocityLimitCloseness);
    }

    protected final void producePositionPathWhileTurningSlowlyTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        testPathWhileTurningSlowly("Position", 600, 20, true, factory,60, imageFilePositionPath);
    }

    protected final void produceAnglesPathWhileTurningSlowlyTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        testPathWhileTurningSlowly("Angles",600, 600, false, factory,1, imageFileAnglesPath);
    }

    private void testPathWhileTurningSlowly(String name, int width, int height, boolean oneScale, BoatPathFactory factory, double time, String imageFileName) {
        final int nPathPoints = 10000;
        final BoatPathUpdater pathUpdater = new BoatPathUpdater(factory.instance(nPathPoints), nSteps);

        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double omg = MathConstants.PI_OVER_TWO/time;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = boatInstance(constants, omg, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, oneScale, path);

        writeToImageFile(name, path.toString(), pixels, imageFileName);
    }

    protected final void produceMultiplePositionPathsWithOmegaVariationTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        multiplePathsTestWithOmegaVariation("Multiple position paths with omega variation",600, 80, true, 4, factory, 60, imageFilePositionPathMultipleOmg);
    }

    protected final void produceMultipleAnglesPathsWithOmegaVariationTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        multiplePathsTestWithOmegaVariation("Multiple angles paths with omega variation", 600, 600, false, 2, factory, 2.05, imageFileAnglesPathMultipleOmg);
    }

    private void multiplePathsTestWithOmegaVariation(String name, int width, int height, boolean oneScale, int omgScaleFactor, BoatPathFactory factory, double time, String imageFileName) {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h

        // Left-slow-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg1 = MathConstants.PI_OVER_TWO/time;
        final UpdatableState underTest1 = boatInstance(constants, omg1, 0, v0, 0, 0, 0);

        // Left-faster-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg2 = omgScaleFactor*omg1;
        final UpdatableState underTest2 = boatInstance(constants, omg2, 0, v0, 0, 0, 0);

        multiplePathsTest(name, width, height, oneScale, underTest1, underTest2, time, factory, imageFileName);
    }

    protected final void produceMultiplePositionPathsWithKRatioVariationTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        multiplePathsTestWithKRatioVariation("Multiple position paths paths with k-ratio variation",600, 40, true, 200, factory,60, imageFilePositionPathMultipleKRatio);
    }

    protected final void produceMultipleAnglesPathsWithKRatioVariationTest() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        multiplePathsTestWithKRatioVariation("Multiple angles paths paths with k-ratio variation",600, 600, false, 5, factory, 2.5, imageFileAnglesPathMultipleKRatio);
    }

    private void multiplePathsTestWithKRatioVariation(String name, int width, int height, boolean oneScale, int kRatScaleFactor, BoatPathFactory factory, double time, String imageFileName) {
        final double kLon = 1;
        final double kLatOverKLon1 = 5;
        final double kLatOverKLon2 = kLatOverKLon1*kRatScaleFactor;

        final BoatConstants constants1 = new BoatConstantsImpl(kLon, kLatOverKLon1, 10);
        final BoatConstants constants2 = new BoatConstantsImpl(kLon, kLatOverKLon2, 10);

        final double v0 = 10; // 36 km/h
        final double omg = Math.PI/time;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest1 = boatInstance(constants1, omg, 0, v0, 0, 0, 0);

        // Left-turning boat, with higher lateral to longitudinal resistance ratio, setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest2 = boatInstance(constants2, omg, 0, v0, 0, 0, 0);

        multiplePathsTest(name, width, height, oneScale, underTest1, underTest2, time, factory, imageFileName);
    }

    private void multiplePathsTest(String name, int width, int height, boolean oneScale, UpdatableState underTest1, UpdatableState underTest2, double time, BoatPathFactory factory, String imageFileName) {
        final int nPathPoints = 10000;

        final BoatPath path1 = update(factory.instance(nPathPoints), time, underTest1);
        final BoatPath path2 = update(factory.instance(nPathPoints), time, underTest2);

        final BoatPath[] paths = setCommonPathLimits(path1, path2);
        final PixelSet[] pixels = toPixelSets(width, height, oneScale, paths);
        final byte[] values = {(byte)127, (byte)255};

        writeToImageFile(name, paths[0].toString(), pixels, values, imageFileName);
    }

    private BoatPath update(BoatPath input, double time, UpdatableState underTest) {
        Assert.assertTrue(input.capacity() > 0);
        Assert.assertEquals(0, input.numberOfPoints());

        final BoatPathUpdater pathUpdater = new BoatPathUpdater(input, nSteps);
        pathUpdater.update(time, underTest);

        final BoatPath updated = pathUpdater.getPath();
        Assert.assertNotNull(updated);
        Assert.assertEquals(input, updated);
        Assert.assertEquals(input.capacity(), updated.numberOfPoints());

        return updated;
    }

    private BoatPath[] setCommonPathLimits(BoatPath... paths) {
        final Limits limits = selectLargestLimits(paths);
        for (BoatPath path : paths) {
            path.resetLimits(limits);
        }

        return paths;
    }

    private Limits selectLargestLimits(BoatPath[] paths) {
        double xMin = paths[0].xMin();
        double xMax = paths[0].xMax();
        double yMin = paths[0].yMin();
        double yMax = paths[0].yMax();

        if (paths.length > 1) {
            for (int i=1 ; i<paths.length ; i++) {
                if (paths[i].xMin() < xMin) {
                    xMin = paths[i].xMin();
                }

                if (paths[i].xMax() > xMax) {
                    xMax = paths[i].xMax();
                }

                if (paths[i].yMin() < yMin) {
                    yMin = paths[i].yMin();
                }

                if (paths[i].yMax() > yMax) {
                    yMax = paths[i].yMax();
                }
            }
        }

        final double xMinMin = xMin;
        final double xMaxMax = xMax;
        final double yMinMin = yMin;
        final double yMaxMax = yMax;

        return new Limits() {
            @Override
            public double xMin() {
                return xMinMin;
            }

            @Override
            public double xMax() {
                return xMaxMax;
            }

            @Override
            public double yMin() {
                return yMinMin;
            }

            @Override
            public double yMax() {
                return yMaxMax;
            }

            @Override
            public void resetLimits(Limits other) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private PixelSet[] toPixelSets(int width, int height, boolean oneScale, BoatPath[] paths) {
        final PixelSet[] pixels = new PixelSet[paths.length];
        for (int i=0 ; i<paths.length; i++) {
            pixels[i] = new PixelSetImpl(width, height, oneScale, paths[i]);
        }

        return pixels;
    }
}
