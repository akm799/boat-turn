package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.math.TrigValues;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 29/01/2018.
 */
public final class BoatVLonPath extends AbstractBoatPath {

    public BoatVLonPath(int nPoints) {
        super(nPoints);
    }

    @Override
    protected void copyStateToPoint(State state, double[] point) {
        final TrigValues hdn = state.hdnTrig();
        point[X_INDEX] = state.t();
        point[Y_INDEX] = state.vx()*hdn.cos() + state.vy()*hdn.sin(); // vLon
    }
}
