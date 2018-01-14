package uk.co.akm.test.motion.boat.path.helper.path;

import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public interface BoatPath {

    void addPoint(State point);

    double xMin();

    double xMax();

    double yMin();

    double yMax();

    int numberOfPoints();

    double[][] pathPoints();
}
