package uk.co.akm.test.motion.boat.quad;

import uk.co.akm.test.motion.boat.math.TrigValues;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.phys.Body;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * A boat that rotates at a pre-defined, constant angular velocity and a resistance to its linear motion proportional to
 * the square of its speed, for high velocities, or proportional to its speed, for low velocities.
 *
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class TurningBoat extends Body {
    private static final double V_TRANSITION = 1;

    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    private double cosa;
    private double sina;
    private double vLon;
    private double vLat;
    private double vLonSqSigned;
    private double vLatSqSigned;

    public TurningBoat(BoatConstants constants, double omgHdn0, double hdn0, double vx0, double vy0, double x0, double y0) {
        super(omgHdn0, 0, 0, hdn0, 0, 0, vx0, vy0, 0, x0, y0, 0);

        kLon = constants.kLon();
        kLat = constants.kLat();
        kLonReverse = constants.kLonReverse();
    }

    @Override
    protected void initUpdate(State start, double dt) {
        final TrigValues hdn = start.hdnTrig();
        cosa = hdn.cos();
        sina = hdn.sin();

        final double vx = start.vx();
        final double vy = start.vy();
        vLon =  vx*cosa + vy*sina;
        vLat = -vx*sina + vy*cosa;
        vLonSqSigned = vLon*Math.abs(vLon);
        vLatSqSigned = vLat*Math.abs(vLat);
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {}

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double kLon = (vLon >= 0 ? this.kLon : this.kLonReverse);

        // Evaluate the acceleration wrt the linear heading.
        final double aLon = estimateLinearResistanceForce(kLon, V_TRANSITION, vLon, vLonSqSigned); // Mass is 1
        final double aLat = estimateLinearResistanceForce(kLat, V_TRANSITION, vLat, vLatSqSigned); // Mass is 1

        // Rotate the acceleration wrt the linear heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina;
        ay = aLon*sina + aLat*cosa;
    }

    private double estimateLinearResistanceForce(double k, double vTransition, double v, double vSqSigned) {
        if (v > vTransition) {
            return -k*vSqSigned;
        } else {
            return -k*v;
        }
    }
}
