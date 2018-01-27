package uk.co.akm.test.motion.boat.test;

import org.junit.Assert;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.path.helper.image.PixelSet;
import uk.co.akm.test.motion.boat.path.helper.image.impl.ImageHelper;
import uk.co.akm.test.motion.boat.path.helper.image.impl.PixelSetImpl;
import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatOmegaPath;
import uk.co.akm.test.motion.boat.path.helper.path.impl.BoatPathUpdater;
import uk.co.akm.test.motion.boat.phys.UpdatableState;

import java.io.File;

/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public abstract class RotatingBoatPathTest {
    private final int nSteps = 1000000; //TODO Move to common super-class.

    private final String baseImageFolder = "./data/image/"; //TODO Move to common super-class.

    private final String imageFileOmegaPath = "boat-omega-path.png";

    protected abstract String imageSubFolder();

    protected abstract BoatConstants boatConstants(double kLon, double kLatOverKLon);

    protected abstract UpdatableState boatInstance(BoatConstants constants, double omgHdn0, double hdn0, double v0);

    protected final void rotationTest(double kLon, double kLatOverKLon, double time, int width, int height) {
        final int nPathPoints = 10000;

        final BoatPathUpdater pathUpdater = new BoatPathUpdater(new BoatOmegaPath(nPathPoints), nSteps);

        final BoatConstants constants = boatConstants(kLon, kLatOverKLon);
        final UpdatableState underTest = boatInstance(constants, 0, 0, constants.getRudderData().v);

        pathUpdater.update(time, underTest);

        final BoatPath path = pathUpdater.getPath();
        Assert.assertNotNull(path);
        Assert.assertEquals(nPathPoints, path.numberOfPoints());

        final PixelSet pixels = new PixelSetImpl(width, height, false, path);

        final File outputImageFile = fileInstance(imageFileOmegaPath);
        if (outputImageFile.exists()) {
            outputImageFile.delete();
        }
        ImageHelper.writeToBufferedImageFile(pixels, outputImageFile);

        Assert.assertTrue(outputImageFile.exists());
        Assert.assertTrue(outputImageFile.length() > 0);

        System.out.println(imageSubFolder() + "> omega: " + path);

    }

    //TODO Move to common super-class.
    private File fileInstance(String imageFileName) {
        final String subFolderName = imageSubFolder();
        final String subFolder = (subFolderName.endsWith("/") ? subFolderName : (subFolderName + "/"));

        return new File(baseImageFolder + subFolder + imageFileName);
    }
}
