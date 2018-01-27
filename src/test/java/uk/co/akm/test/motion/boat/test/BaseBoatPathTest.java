package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.ImageHelper;

import java.io.File;

/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public abstract class BaseBoatPathTest {
    protected final int nSteps = 1000000;

    private final String baseImageFolder = "./data/image/";

    protected abstract String imageSubFolder();

    protected final void writeToImageFile(String testName, String pathDesc, PixelSet pixels, String imageFileName) {
        final File outputImageFile = createNewFile(imageFileName);

        ImageHelper.writeToBufferedImageFile(pixels, outputImageFile);

        assertWrittenToFile(outputImageFile);
        System.out.println(imageSubFolder() + "> " + testName + ": " + pathDesc);
    }

    protected final void writeToImageFile(String testName, String pathDesc, PixelSet[] pixels, byte[] values, String imageFileName) {
        final File outputImageFile = createNewFile(imageFileName);

        ImageHelper.writeToBufferedImageFile(pixels, values, outputImageFile);

        assertWrittenToFile(outputImageFile);
        System.out.println(imageSubFolder() + "> " + testName + ": " + pathDesc);
    }

    private File createNewFile(String imageFileName) {
        final File outputImageFile = fileInstance(imageFileName);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }

        return outputImageFile;
    }

    private final File fileInstance(String imageFileName) {
        final String subFolderName = imageSubFolder();
        final String subFolder = (subFolderName.endsWith("/") ? subFolderName : (subFolderName + "/"));

        return new File(baseImageFolder + subFolder + imageFileName);
    }

    private void assertWrittenToFile(File outputImageFile) {
        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);
    }
}
