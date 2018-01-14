package uk.co.akm.test.motion.boat.path.helper;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public interface BoatPath {

    double xMin();

    double xMax();

    double yMin();

    double yMax();

    int numberOfPoints();

    double[][] pathPoints();
}
