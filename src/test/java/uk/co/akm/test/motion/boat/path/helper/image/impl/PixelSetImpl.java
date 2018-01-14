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

    public PixelSetImpl(int width, int height, BoatPath path) {
        this.width = width;
        this.height = height;
        this.nonBlankPixels = new int[path.numberOfPoints()][N_DIM];

        setPixels(path);
    }

    private void setPixels(BoatPath path) {
        final double xf = width/(path.xMax() - path.xMin());
        final double yf = height/(path.yMax() - path.yMin());

        final double[][] points = path.pathPoints();
        for (int i=0 ; i<path.numberOfPoints() ; i++) {
            final int xp = (int)Math.round(points[i][X_INDEX]*xf);
            final int yp = height - (int)Math.round(points[i][Y_INDEX]*yf);
            nonBlankPixels[i][X_INDEX] = xp;
            nonBlankPixels[i][Y_INDEX] = yp;
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
