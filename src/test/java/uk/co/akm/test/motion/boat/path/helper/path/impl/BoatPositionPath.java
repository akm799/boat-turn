package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatPositionPath extends AbstractBoatPath {

    public BoatPositionPath(int nPoints) {
        super(nPoints);
    }

    @Override
    protected void copyStateToPoint(State state, double[] point) {
        point[X_INDEX] = state.x();
        point[Y_INDEX] = state.y();
    }
}
