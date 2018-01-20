package uk.co.akm.test.motion.boat.path.helper.image.impl;

import uk.co.akm.test.motion.boat.path.helper.dim.DimSpecs;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class PixelSetImpl implements PixelSet, DimSpecs {
    private final int width;
    private final int height;
    private final int[][] nonBlankPixels;

    public PixelSetImpl(int width, int height, boolean oneScale, BoatPath path) {
        this.width = width;
        this.height = height;
        this.nonBlankPixels = new int[path.numberOfPoints()][N_DIM];

        setPixels(path, oneScale);
    }

    private void setPixels(BoatPath path, boolean oneScale) {
        final double xMin = path.xMin();
        final double yMin = path.yMin();
        final double xf = width/(path.xMax() - xMin);
        final double yf = height/(path.yMax() - yMin);
        final double f = Math.min(xf, yf);

        final double xScale = (oneScale ? f : xf);
        final double yScale = (oneScale ? f : yf);

        final double[][] points = path.pathPoints();
        for (int i=0 ; i<path.numberOfPoints() ; i++) {
            final int xp = (int)Math.floor((points[i][X_INDEX] - xMin)*xScale);
            final int yp = height - (int)Math.floor((points[i][Y_INDEX] - yMin)*yScale);
            nonBlankPixels[i][X_INDEX] = xp;
            nonBlankPixels[i][Y_INDEX] = yp;

            if (xp == width) {
                nonBlankPixels[i][X_INDEX] = width - 1;
            }

            if (yp == height) {
                nonBlankPixels[i][Y_INDEX] = height - 1;
            }
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int[][] nonBlankPixels() {
        return nonBlankPixels;
    }
}
