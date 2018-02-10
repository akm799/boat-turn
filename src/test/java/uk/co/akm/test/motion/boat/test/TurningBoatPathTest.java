package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.math.MathConstants;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatAnglesPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPositionPath;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by Thanos Mavroidis on 20/01/2018.
 */
public abstract class TurningBoatPathTest extends BaseComparativeBoatPathTest {
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

        final double omg1 = MathConstants.PI_OVER_TWO/time;
        final double omg2 = omgScaleFactor*omg1;
        final Collection<UpdatableState> testables = new ArrayList<>(2);
        testables.add(boatInstance(constants, omg1, 0, v0, 0, 0, 0));
        testables.add(boatInstance(constants, omg2, 0, v0, 0, 0, 0));

        multiplePathsTest(name, width, height, oneScale, testables, time, factory, imageFileName);
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

        final Collection<UpdatableState> testables = new ArrayList<>(2);
        testables.add(boatInstance(constants1, omg, 0, v0, 0, 0, 0));
        testables.add(boatInstance(constants2, omg, 0, v0, 0, 0, 0));

        multiplePathsTest(name, width, height, oneScale, testables, time, factory, imageFileName);
    }
}
