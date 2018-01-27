package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 27/01/2018.
 */
public final class BoatOmegaPath extends AbstractBoatPath {

    public BoatOmegaPath(int nPoints) {
        super(nPoints);
    }

    @Override
    protected void copyStateToPoint(State state, double[] point) {
        point[X_INDEX] = state.t();
        point[Y_INDEX] = state.omgHdn();
    }
}
