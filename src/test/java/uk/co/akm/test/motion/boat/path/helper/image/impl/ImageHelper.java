package uk.co.akm.test.motion.boat.path.helper.image.impl;

import uk.co.akm.test.motion.boat.path.helper.dim.DimSpecs;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public class ImageHelper implements DimSpecs {
    private static final byte BLANK_PIXEL_VALUE = (byte)0;
    private static final byte SET_PIXEL_VALUE = (byte)255;

    public static void writeToBufferedImageFile(PixelSet pixels, File outputFile) {
        final RenderedImage image = toBufferedImage(pixels);

        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private static BufferedImage toBufferedImage(PixelSet pixels) {
        final int width = pixels.width();
        final int height = pixels.height();
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

        Arrays.fill(data, BLANK_PIXEL_VALUE);
        setNonBlankPixels(pixels, data);

        return image;
    }

    private static void setNonBlankPixels(PixelSet pixels, byte[] data) {
        final int width = pixels.width();
        for (int[] p : pixels.nonBlankPixels()) {
            data[p[Y_INDEX]*width + p[X_INDEX]] = SET_PIXEL_VALUE;
        }
    }

    private ImageHelper() {}
}
