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
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

import java.io.File;
import java.io.UncheckedIOException;

/**
 * //TODO Add test that produces multiple plot: same angular velocity but with different kLat/kLon ratios.
 * //TODO Add test that produces multiple plot: (boat heading vs time) and (velocity vector heading vs time).
 *
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public class TurningBoatPathTest {
    private final int nSteps = 1000000;
    private final String imageFilePath = "./data/image/boat-path.png";
    private final String imageFilePathMultiple = "./data/image/boat-path-comparison.png";

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
    public void shouldProducePathWhileTurningSlowly() {
        final int nPathPoints = 10000;
        final BoatPathUpdater pathUpdater = new BoatPathUpdater(nSteps, nPathPoints);

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

        final PixelSet pixels = new PixelSetImpl(600, 20, path);

        final File outputImageFile = new File(imageFilePath);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        ImageHelper.writeToBufferedImageFile(pixels, outputImageFile);

        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);

        System.out.println(path);
    }

    @Test
    public void shouldProduceMultiplePaths() {
        final int width = 600;
        final int height = 80;

        final int nPathPoints = 10000;

        final double kLon = 1;
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10);

        final double v0 = 10; // 36 km/h
        final double time = 60; // 1 min (enough to slow down)

        // Left-slow-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg1 = MathConstants.PI_OVER_TWO/time;
        final UpdatableState underTest1 = new TurningBoat(constants, omg1, 0, v0, 0, 0, 0);
        final BoatPath path1 = update(nPathPoints, time, underTest1);

        // Left-faster-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final double omg2 = 4*omg1;
        final UpdatableState underTest2 = new TurningBoat(constants, omg2, 0, v0, 0, 0, 0);
        final BoatPath path2 = update(nPathPoints, time, underTest2);

        final BoatPath[] paths = setCommonPathLimits(path1, path2);
        final PixelSet[] pixels = toPixelSets(width, height, paths);
        final byte[] values = {(byte)127, (byte)255};

        final File outputImageFile = new File(imageFilePathMultiple);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        ImageHelper.writeToBufferedImageFile(pixels, values, outputImageFile);

        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);
    }

    private BoatPath update(int nPathPoints, double time, UpdatableState underTest) {
        final BoatPathUpdater pathUpdater = new BoatPathUpdater(nSteps, nPathPoints);
        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        return path;
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
}
