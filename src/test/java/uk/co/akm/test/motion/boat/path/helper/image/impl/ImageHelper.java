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

    public static void writeToBufferedImageFile(PixelSet[] pixels, byte[] values, File outputFile) {
        final RenderedImage image = toBufferedImage(pixels, values);

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
        setNonBlankPixels(pixels, SET_PIXEL_VALUE, data);

        return image;
    }

    private static BufferedImage toBufferedImage(PixelSet[] pixels, byte[] values) {
        checkArgs(pixels, values);

        final int width = pixels[0].width();
        final int height = pixels[0].height();
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

        Arrays.fill(data, BLANK_PIXEL_VALUE);

        for (int i=0 ; i<pixels.length ; i++) {
            setNonBlankPixels(pixels[i], values[i], data);
        }

        return image;
    }

    private static void setNonBlankPixels(PixelSet pixels, byte value, byte[] data) {
        final int width = pixels.width();
        for (int[] p : pixels.nonBlankPixels()) {
            data[p[Y_INDEX]*width + p[X_INDEX]] = value;
        }
    }

    private static void checkArgs(PixelSet[] pixels, byte[] values) {
        if (pixels == null || pixels.length == 0 || values == null || values.length == 0) {
            throw new IllegalArgumentException("Null or empty pixels set array and/or byte values array.");
        }

        if (pixels.length != values.length) {
            throw new IllegalArgumentException("Null or pixels set array (length=" + pixels.length + ") does not have the same length as the  byte values array (length=" + values.length + ").");
        }

        final int width = pixels[0].width();
        final int height = pixels[0].height();
        for (PixelSet p : pixels) {
            if (p.width() != width || p.height() != height) {
                throw new IllegalArgumentException("Pixel set array members do not all have the same width and height.");
            }
        }
    }

    private ImageHelper() {}
}
