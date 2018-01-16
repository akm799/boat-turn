package uk.co.akm.test.motion.boat.path;

import org.junit.Assert;
import org.junit.Test;
import uk.co.akm.test.motion.boat.linear.TurningBoat;
import uk.co.akm.test.motion.boat.math.MathConstants;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.ImageHelper;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.Limits;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatAnglesPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPositionPath;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

import java.io.File;

/**
 * //TODO Add test that produces multiple plot: same angular velocity but with different kLat/kLon ratios.
 * //TODO Add test that produces multiple plot: (boat heading vs time) and (velocity vector heading vs time).
 *
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public class TurningBoatPathTest {
    private final int nSteps = 1000000;
    private final String imageFileAnglesPath = "./data/image/boat-angles-path.png";
    private final String imageFilePositionPath = "./data/image/boat-position-path.png";
    private final String imageFileAnglesPathMultipleOmg = "./data/image/boat-angles-path-omg-comparison.png";
    private final String imageFilePositionPathMultipleOmg = "./data/image/boat-position-path-omg-comparison.png";
    private final String imageFileAnglesPathMultipleKRatio = "./data/image/boat-angles-path-k-ratio-comparison.png";
    private final String imageFilePositionPathMultipleKRatio = "./data/image/boat-position-path-k-ratio-comparison.png";

    @Test
    public void shouldComeToRestWhileTurningWhileTurningSlowly() {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)
        final double omg = MathConstants.PI_OVER_TWO/time;

        final double velocityLimitCloseness = 1.0E-25;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = new TurningBoat(constants, omg, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        Assert.assertFalse(underTest.v() < 0);
        Assert.assertTrue(underTest.v() < velocityLimitCloseness);
    }

    @Test
    public void shouldProducePositionPathWhileTurningSlowly() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatPositionPath(nPoints);
            }
        };

        testPathWhileTurningSlowly("Position", 600, 20, factory, imageFilePositionPath);
    }

    @Test
    public void shouldProduceAnglesPathWhileTurningSlowly() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatAnglesPath(nPoints);
            }
        };

        testPathWhileTurningSlowly("Angles",600, 20, factory, imageFileAnglesPath);
    }

    private void testPathWhileTurningSlowly(String name, int width, int height, BoatPathFactory factory, String imageFileName) {
        final int nPathPoints = 10000;
        final BoatPathUpdater pathUpdater = new BoatPathUpdater(factory.instance(nPathPoints), nSteps);

        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)
        final double omg = MathConstants.PI_OVER_TWO/time;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = new TurningBoat(constants, omg, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, path);

        final File outputImageFile = new File(imageFileName);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        ImageHelper.writeToBufferedImageFile(pixels, outputImageFile);

        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);

        System.out.println(name + ": " + path);
    }

    @Test
    public void shouldProduceMultiplePositionPathsWithOmegaVariation() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatPositionPath(nPoints);
            }
        };

        multiplePathsTestWithOmegaVariation("Multiple position paths with omega variation",600, 80, 4, factory, imageFilePositionPathMultipleOmg);
    }

    @Test
    public void shouldProduceMultipleAnglesPathsWithOmegaVariation() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatAnglesPath(nPoints);
            }
        };

        multiplePathsTestWithOmegaVariation("Multiple angles paths with omega variation", 600, 100, 32, factory, imageFileAnglesPathMultipleOmg);
    }

    private void multiplePathsTestWithOmegaVariation(String name, int width, int height, int omgScaleFactor, BoatPathFactory factory, String imageFileName) {
        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)

        // Left-slow-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg1 = MathConstants.PI_OVER_TWO/time;
        final UpdatableState underTest1 = new TurningBoat(constants, omg1, 0, v0, 0, 0, 0);

        // Left-faster-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg2 = omgScaleFactor*omg1;
        final UpdatableState underTest2 = new TurningBoat(constants, omg2, 0, v0, 0, 0, 0);

        multiplePathsTest(name, width, height, underTest1, underTest2, time, factory, imageFileName);
    }

    @Test
    public void shouldProduceMultiplePositionPathsWithKRatioVariation() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatPositionPath(nPoints);
            }
        };

        multiplePathsTestWithKRatioVariation("Multiple position paths paths with k-ratio variation",600, 40, 200, factory, imageFilePositionPathMultipleKRatio);
    }

    @Test
    public void shouldProduceMultipleAnglesPathsWithKRatioVariation() {
        final BoatPathFactory factory = new BoatPathFactory() {
            @Override
            public BoatPath instance(int nPoints) {
                return new BoatAnglesPath(nPoints);
            }
        };

        multiplePathsTestWithKRatioVariation("Multiple angles paths paths with k-ratio variation",600, 10, 200, factory, imageFileAnglesPathMultipleKRatio);
    }

    private void multiplePathsTestWithKRatioVariation(String name, int width, int height, int kRatScaleFactor, BoatPathFactory factory, String imageFileName) {
        final double kLon = 1;
        final double kLatOverKLon1 = 5;
        final double kLatOverKLon2 = kLatOverKLon1*kRatScaleFactor;

        final BoatConstants constants1 = new BoatConstantsImpl(kLon, kLatOverKLon1, 10);
        final BoatConstants constants2 = new BoatConstantsImpl(kLon, kLatOverKLon2, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)
        final double omg = Math.PI/time;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest1 = new TurningBoat(constants1, omg, 0, v0, 0, 0, 0);

        // Left-turning boat, with higher lateral to longitudinal resistance ratio, setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest2 = new TurningBoat(constants2, omg, 0, v0, 0, 0, 0);

        multiplePathsTest(name, width, height, underTest1, underTest2, time, factory, imageFileName);
    }

    private void multiplePathsTest(String name, int width, int height, UpdatableState underTest1, UpdatableState underTest2, double time, BoatPathFactory factory, String imageFileName) {
        final int nPathPoints = 10000;

        final BoatPath path1 = update(factory.instance(nPathPoints), time, underTest1);
        final BoatPath path2 = update(factory.instance(nPathPoints), time, underTest2);

        final BoatPath[] paths = setCommonPathLimits(path1, path2);
        final PixelSet[] pixels = toPixelSets(width, height, paths);
        final byte[] values = {(byte)127, (byte)255};

        final File outputImageFile = new File(imageFileName);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        ImageHelper.writeToBufferedImageFile(pixels, values, outputImageFile);

        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);

        System.out.println(name + ": " + paths[0]);
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

    private PixelSet[] toPixelSets(int width, int height, BoatPath[] paths) {
        final PixelSet[] pixels = new PixelSet[paths.length];
        for (int i=0 ; i<paths.length; i++) {
            pixels[i] = new PixelSetImpl(width, height, paths[i]);
        }

        return pixels;
    }

    interface BoatPathFactory {
        BoatPath instance(int nPoints);
    }
}
