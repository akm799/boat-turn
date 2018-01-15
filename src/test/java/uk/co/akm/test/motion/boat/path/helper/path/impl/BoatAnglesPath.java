package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.math.MathConstants;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * Created by Thanos Mavroidis on 15/01/2018.
 */
public final class BoatAnglesPath extends AbstractBoatPath {

    public BoatAnglesPath(int nPoints) {
        super(nPoints);
    }

    @Override
    protected void copyStateToPoint(State state, double[] point) {
        final double a = state.hdnP();
        final double va = getVelocityVectorAngle(state);

        point[X_INDEX] = state.t();
        point[Y_INDEX] = a - va;
    }

    private double getVelocityVectorAngle(State state) {
        final double vx = state.vx();
        final double vy = state.vy();
        if (vx != 0) {
            return Math.atan2(vy, vx);
        } else {
            return (vy >= 0 ? MathConstants.PI_OVER_TWO : -MathConstants.PI_OVER_TWO);
        }
    }
}
