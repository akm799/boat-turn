package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.Limits;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.phys.UpdatableState;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Created by Thanos Mavroidis on 09/02/2018.
 */
public abstract class BaseComparativeBoatPathTest extends BaseBoatPathTest {

    protected final void multiplePathsTest(String name, int width, int height, boolean oneScale, Collection<UpdatableState> testables, double time, BoatPathFactory factory, String imageFileName) {
        final int nPathPoints = 10000;

        final java.util.function.Function<UpdatableState, BoatPath> pathProducer = (UpdatableState underTest) -> {
            return update(factory.instance(nPathPoints), time, underTest);
        };

        final java.util.function.Function<BoatPath, PixelSet> pixelSetProducer = (BoatPath path) -> {
            return new PixelSetImpl(width, height, oneScale, path);
        };

        final Collection<BoatPath> paths = testables.stream().map(pathProducer).collect(Collectors.toList());
        setCommonPathLimits(paths);

        final Collection<PixelSet> pixels = paths.stream().map(pixelSetProducer).collect(Collectors.toList());

        final byte[] shades = shades(pixels.size());
        final PixelSet[] pixelsArray = pixels.toArray(new PixelSet[0]);
        final String description = pixelsArray[0].toString();

        writeToImageFile(name, description, pixelsArray, shades, imageFileName);
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

    private void setCommonPathLimits(Collection<BoatPath> paths) {
        final Limits limits = selectLargestLimits(paths.iterator());
        paths.stream().forEach(p -> p.resetLimits(limits));
    }

    private Limits selectLargestLimits(Iterator<BoatPath> paths) {
        final BoatPath first = paths.next();

        double xMin = first.xMin();
        double xMax = first.xMax();
        double yMin = first.yMin();
        double yMax = first.yMax();

        while (paths.hasNext()) {
            final BoatPath path = paths.next();

            if (path.xMin() < xMin) {
                xMin = path.xMin();
            }

            if (path.xMax() > xMax) {
                xMax = path.xMax();
            }

            if (path.yMin() < yMin) {
                yMin = path.yMin();
            }

            if (path.yMax() > yMax) {
                yMax = path.yMax();
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

    private byte[] shades(int number) {
        final int max = 255;
        final int distance = max/number;

        final byte[] shades = new byte[number];
        for (int i=0 ; i<number ; i++) {
            shades[i] = (byte)(max - i*distance);
        }

        return shades;
    }
}
