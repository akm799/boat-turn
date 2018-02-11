package uk.co.akm.test.motion.boat.quad;

import org.junit.Assert;
import org.junit.Test;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.Rotation;
import uk.co.akm.test.motion.boat.model.RudderData;
import uk.co.akm.test.motion.boat.model.impl.BoatConstantsImpl;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatAnglesPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPositionPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatSpeedPath;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;
import uk.co.akm.test.motion.boat.test.BoatPathFactory;
import uk.co.akm.test.motion.boat.test.BaseComparativeBoatPathTest;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 08/02/2018.
 */
public class BoatPathQuadTest extends BaseComparativeBoatPathTest {
    private final String imageFileSpeedPath = "boat-speed-path.png";
    private final String imageFileAnglesPath = "boat-angles-path.png";
    private final String imageFilePositionPath = "boat-position-path.png";
    private final String imageFileAnglesPathMultipleRudderForce = "boat-angles-path-rudder-force-comparison.png";
    private final String imageFilePositionPathMultipleRudderForce = "boat-position-path-rudder-force-comparison.png";
    private final String imageFileAnglesPathMultipleRudderDeflection = "boat-angles-path-rudder-deflection-comparison.png";
    private final String imageFilePositionPathMultipleRudderDeflection = "boat-position-path-rudder-deflection-comparison.png";
    private final String imageFileAnglesPathMultipleKRatio = "boat-angles-path-k-ratio-comparison.png";
    private final String imageFilePositionPathMultipleKRatio = "boat-position-path-k-ratio-comparison.png";
    private final String imageFileAnglesPathMultipleV0 = "boat-angles-path-v0-comparison.png";
    private final String imageFilePositionPathMultipleV0 = "boat-position-path-v0-comparison.png";

    private final double kLon = 1;
    private final RudderData rudderData = new RudderData(4, 1.5, 3*Math.PI/8, 2.5, 1);
    private final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10, rudderData);

    private final double v0 = 10; // 36 km/h

    protected String imageSubFolder() {
        return "quad/move";
    }

    @Test
    public final void shouldComeToRestWhileTurning() {
        final double time = 120; // 2 mins (enough to slow down)
        final double velocityLimitCloseness = 1.0E-50;

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        Updater.update(underTest, time, nSteps);

        // Boat has slowed down to zero velocity.
        Assert.assertFalse(underTest.v() < 0);
        Assert.assertTrue(underTest.v() < velocityLimitCloseness);
    }

    @Test
    public void shouldProducePositionPathWhileTurningSlowly() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        testPathWhileTurningSlowly("Position", 400, 600, true, factory,60, imageFilePositionPath);
    }

    @Test
    public void shouldProduceAnglesPathWhileTurningSlowly() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        testPathWhileTurningSlowly("Angles",600, 600, false, factory,3, imageFileAnglesPath);
    }

    @Test
    public void shouldProduceSpeedPathWhileTurningSlowly() {
        final BoatPathFactory factory = (int nPoints) -> new BoatSpeedPath(nPoints);
        testPathWhileTurningSlowly("Speed",600, 600, false, factory,2, imageFileSpeedPath);
    }

    private void testPathWhileTurningSlowly(String name, int width, int height, boolean oneScale, BoatPathFactory factory, double time, String imageFileName) {
        final int nPathPoints = 10000;
        final BoatPathUpdater pathUpdater = new BoatPathUpdater(factory.instance(nPathPoints), nSteps);

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        final UpdatableState underTest = new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);

        // Turning boat slows down.
        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, oneScale, path);

        writeToImageFile(name, path.toString(), pixels, imageFileName);
    }

    @Test
    public void shouldProduceAnglesPathsWhileTurningSlowlyForRudderForceComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        rudderForceComparisonPathTests(factory, "Multiple angles paths with rudder force variation", false, 0.3, imageFileAnglesPathMultipleRudderForce);
    }

    @Test
    public void shouldProducePositionPathsWhileTurningSlowlyForRudderForceComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        rudderForceComparisonPathTests(factory, "Multiple position paths with rudder force variation", true, 60, imageFilePositionPathMultipleRudderForce);
    }

    private void rudderForceComparisonPathTests(BoatPathFactory factory, String name, boolean oneScale, double time, String imageFileName) {
        final double omg1 = 2.5*Math.PI/8;
        final double omg2 = Math.PI/2;
        final double omg3 = Math.PI;

        final Collection<UpdatableState> testables = new ArrayList<>(3);
        testables.add(boatInstanceForRudderForceComparison(omg1));
        testables.add(boatInstanceForRudderForceComparison(omg2));
        testables.add(boatInstanceForRudderForceComparison(omg3));

        multiplePathsTest(name, 600, 600, oneScale, testables, time, factory, imageFileName);
    }

    private UpdatableState boatInstanceForRudderForceComparison(double omega) {
        final RudderData rudderData = new RudderData(4, 1.5, omega, 2.5, 1);
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10, rudderData);

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        return new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);
    }

    @Test
    public void shouldProduceAnglesPathsWhileTurningSlowlyForRudderDefelctionComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        rudderDeflectionComparisonPathTests(factory, "Multiple angles paths with rudder deflection variation", false, 0.3, imageFileAnglesPathMultipleRudderDeflection);
    }

    @Test
    public void shouldProducePositionPathsWhileTurningSlowlyForRudderDeflectionComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        rudderDeflectionComparisonPathTests(factory, "Multiple position paths with rudder deflection variation", true, 60, imageFilePositionPathMultipleRudderDeflection);
    }

    private void rudderDeflectionComparisonPathTests(BoatPathFactory factory, String name, boolean oneScale, double time, String imageFileName) {
        final double deflection1 = -1.0;
        final double deflection2 = -0.75;
        final double deflection3 = -0.5;
        final double deflection4 = -0.25;
        final double deflection5 =  0;
        final double deflection6 =  0.25;
        final double deflection7 =  0.5;
        final double deflection8 =  0.75;
        final double deflection9 =  1.0;

        final Collection<UpdatableState> testables = new ArrayList<>(9);
        testables.add(boatInstanceForRudderDeflectionComparison(deflection1));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection2));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection3));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection4));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection5));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection6));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection7));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection8));
        testables.add(boatInstanceForRudderDeflectionComparison(deflection9));

        multiplePathsTest(name, 600, 600, oneScale, testables, time, factory, imageFileName);
    }

    private UpdatableState boatInstanceForRudderDeflectionComparison(double deflection) {
        return new Boat(constants, deflection, 0, v0, 0, 0, 0);
    }

    @Test
    public void shouldProduceAnglesPathsWhileTurningSlowlyForKRatioComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        kRatioComparisonPathTests(factory, "Multiple position paths with k-ratio variation", false, 2, imageFileAnglesPathMultipleKRatio);
    }

    @Test
    public void shouldProducePositionPathsWhileTurningSlowlyForKRatioComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        kRatioComparisonPathTests(factory, "Multiple position paths with k-ratio variation", true, 60, imageFilePositionPathMultipleKRatio);
    }

    private void kRatioComparisonPathTests(BoatPathFactory factory, String name, boolean oneScale, double time, String imageFileName) {
        final double kLatOverKLon1 = 2;
        final double kLatOverKLon2 = 10;
        final double kLatOverKLon3 = 50;
        final double kLatOverKLon4 = 150;

        final Collection<UpdatableState> testables = new ArrayList<>(4);
        testables.add(boatInstanceForKRatioComparison(kLatOverKLon1));
        testables.add(boatInstanceForKRatioComparison(kLatOverKLon2));
        testables.add(boatInstanceForKRatioComparison(kLatOverKLon3));
        testables.add(boatInstanceForKRatioComparison(kLatOverKLon4));

        multiplePathsTest(name, 600, 600, oneScale, testables, time, factory, imageFileName);
    }

    private UpdatableState boatInstanceForKRatioComparison(double kLatOverKLon) {
        final BoatConstants constants = new BoatConstantsImpl(kLon, kLatOverKLon, 10, rudderData);

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        return new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);
    }

    @Test
    public void shouldProduceAnglesPathsWhileTurningSlowlyForV0Comparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatAnglesPath(nPoints);
        v0ComparisonPathTests(factory, "Multiple position paths with v0 variation", false, 0.4, imageFileAnglesPathMultipleV0);
    }

    @Test
    public void shouldProducePositionPathsWhileTurningSlowlyForV0Comparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        v0ComparisonPathTests(factory, "Multiple position paths with v0 variation", true, 60, imageFilePositionPathMultipleV0);
    }

    private void v0ComparisonPathTests(BoatPathFactory factory, String name, boolean oneScale, double time, String imageFileName) {
        final double v01 = 0.999;
        final double v02 = 5;
        final double v03 = 10;
        final double v04 = 15;
        final double v05 = 20;

        final Collection<UpdatableState> testables = new ArrayList<>(5);
        testables.add(boatInstanceForInitialSpeedComparison(v01));
        testables.add(boatInstanceForInitialSpeedComparison(v02));
        testables.add(boatInstanceForInitialSpeedComparison(v03));
        testables.add(boatInstanceForInitialSpeedComparison(v04));
        testables.add(boatInstanceForInitialSpeedComparison(v05));

        multiplePathsTest(name, 600, 600, oneScale, testables, time, factory, imageFileName);
    }

    private UpdatableState boatInstanceForInitialSpeedComparison(double v0) {
        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        return new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);
    }
}
