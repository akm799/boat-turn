package uk.co.akm.test.motion.boat.path.helper.path;

import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public interface BoatPath extends Limits {

    void addPoint(State point);

    int numberOfPoints();

    double[][] pathPoints();
}
