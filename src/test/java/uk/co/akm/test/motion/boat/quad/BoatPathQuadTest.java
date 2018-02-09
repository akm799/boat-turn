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
    private final String imageFileAnglesPathMultipleRud = "boat-angles-path-rud-comparison.png";
    private final String imageFilePositionPathMultipleRud = "boat-position-path-rud-comparison.png";

    private final double kLon = 1;
    private final RudderData rudderData = new RudderData(4, 1.5, 3*Math.PI/8, 2.5, 1);
    private final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10, rudderData);

    protected String imageSubFolder() {
        return "quad/move";
    }

    @Test
    public final void shouldComeToRestWhileTurning() {
        final double v0 = 10; // 36 km/h
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

        final double v0 = 10; // 36 km/h

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
        rudderComparisonPathTests(factory, "Multiple angles paths with rudder force variation", false, 0.3, imageFileAnglesPathMultipleRud);
    }

    @Test
    public void shouldProducePositionPathsWhileTurningSlowlyForRudderForceComparison() {
        final BoatPathFactory factory = (int nPoints) -> new BoatPositionPath(nPoints);
        rudderComparisonPathTests(factory, "Multiple position paths with rudder force variation", true, 60, imageFilePositionPathMultipleRud);
    }

    private void rudderComparisonPathTests(BoatPathFactory factory, String name, boolean oneScale, double time, String imageFileName) {
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
        final double v0 = 10; // 36 km/h
        final RudderData rudderData = new RudderData(4, 1.5, omega, 2.5, 1);
        final BoatConstants constants = new BoatConstantsImpl(kLon, 50, 10, rudderData);

        // Left-turning boat setting of from the origin with an initial speed v0 along the x-axis direction.
        return new Boat(constants, Rotation.LEFT, 0, v0, 0, 0, 0);
    }
}
