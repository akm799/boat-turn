package uk.co.akm.test.motion.boat.quad;

import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.Rotation;
import uk.co.akm.test.motion.boat.model.RudderData;
import uk.co.akm.test.motion.boat.phys.Body;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * A boat that moves at a constant velocity but rotates depending on the value of that velocity. To achieve this effect,
 * only the angular acceleration is updated whereas the linear acceleration is always zero. This implementation is meant
 * for studying only the boat rotation characteristics and not its linear motion.
 *
 * Created by Thanos Mavroidis on 26/01/2018.
 */
public final class RotatingBoat extends Body {
    private static final double V_TRANSITION = 1;

    private final double kLat;
    private final double kRud;
    private final RudderData rudderData;

    private final double kLowBack;
    private final double kLowFront;
    private final double kHighBack;
    private final double kHighFront;
    private final double omegaTransitionBack;
    private final double omegaTransitionFront;

    private double v;
    private double vSqSigned;
    private double omg;
    private double omgSqSigned;

    public RotatingBoat(BoatConstants constants, Rotation rotation, double omgHdn0, double hdn0, double v0) {
        super(omgHdn0, 0, 0, hdn0, 0, 0, v0, 0, 0, 0, 0, 0);

        kLat = constants.kLat();
        kRud = rotation.sign*Math.abs(constants.kRud());
        rudderData = constants.getRudderData();

        final double l4 = 4*rudderData.length;
        final double l8 = 2*l4;

        final double x = rudderData.cogDistanceFromStern;
        final double lx = (rudderData.length - x);

        final double x3 = Math.pow(x, 3);
        final double x4 = x*x3;

        final double lx3 = Math.pow(lx, 3);
        final double lx4 = lx*lx3;

        kLowBack = kLat*x3/l4;
        kHighBack = kLat*x4/(l8);
        kLowFront = kLat*lx3/l4;
        kHighFront = kLat*lx4/l8;

        omegaTransitionBack = 2*V_TRANSITION/x;
        omegaTransitionFront = 2*V_TRANSITION/lx;
    }

    @Override
    protected void initUpdate(State start, double dt) {
        v = v();
        vSqSigned = v*Math.abs(v);

        omg = start.omgHdn();
        omgSqSigned = omg*Math.abs(omg);
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {
        final double rudderForce = estimateForce(kRud, V_TRANSITION, v, vSqSigned);
        final double rudderTorque = rudderForce*rudderData.cogDistanceFromStern;

        final double resistanceTorque = estimateResistanceTorque(omg, omgSqSigned);

        final double totalTorque = rudderTorque + resistanceTorque;

        aHdn = totalTorque; // Assume moment of inertia value of 1.
    }

    @Override
    protected void updateAcceleration(State start, double dt) {}

    private double estimateForce(double k, double vTransition, double v, double vSqSigned) {
        if (Math.abs(v) > vTransition) {
            return k*vSqSigned;
        } else {
            return k*v;
        }
    }

    private double estimateResistanceTorque(double omg, double omgSqSigned) {
        return estimateResistanceTorqueBack(omg, omgSqSigned) + estimateResistanceTorqueFront(omg, omgSqSigned);
    }

    private double estimateResistanceTorqueBack(double omg, double omgSqSigned) {
        return estimateResistanceTorque(kHighBack, kLowBack, omegaTransitionBack, omg, omgSqSigned);
    }

    private double estimateResistanceTorqueFront(double omg, double omgSqSigned) {
        return estimateResistanceTorque(kHighFront, kLowFront, omegaTransitionFront, omg, omgSqSigned);
    }

    private double estimateResistanceTorque(double kHigh, double kLow, double omgTransition, double omg, double omgSqSigned) {
        if (Math.abs(omg) > omgTransition) {
            return -kHigh*omgSqSigned;
        } else {
            return -kLow*omg;
        }
    }
}
