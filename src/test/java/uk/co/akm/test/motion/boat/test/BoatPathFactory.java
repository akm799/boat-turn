package uk.co.akm.test.motion.boat.test;

import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;

/**
 * Created by Thanos Mavroidis on 29/01/2018.
 */
public interface BoatPathFactory {
    BoatPath instance(int nPoints);
}
