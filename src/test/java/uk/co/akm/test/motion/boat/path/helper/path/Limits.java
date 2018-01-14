package uk.co.akm.test.motion.boat.path.helper.path;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public interface Limits {

    double xMin();

    double xMax();

    double yMin();

    double yMax();

    void resetLimits(Limits other);
}
