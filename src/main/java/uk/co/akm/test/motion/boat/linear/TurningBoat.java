package uk.co.akm.test.motion.boat.linear;

import uk.co.akm.test.motion.boat.math.TrigValues;
import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.phys.Body;
import uk.co.akm.test.motion.boat.phys.State;

/**
 * A boat that rotates at a pre-defined, constant angular velocity.
 *
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class TurningBoat extends Body {
    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    private double cosa;
    private double sina;
    private double vLon;
    private double vLat;

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
    }

    @Override
    protected void updateAngularAcceleration(State start, double dt) {}

    @Override
    protected void updateAcceleration(State start, double dt) {
        final double kLon = (vLon >= 0 ? this.kLon : this.kLonReverse);

        // Evaluate the acceleration wrt the linear heading.
        final double aLon = -kLon*vLon; // Mass is 1
        final double aLat = -kLat*vLat; // Mass is 1

        // Rotate the acceleration wrt the linear heading an angle a (i.e. the reverse of our previous rotation) to get the acceleration wrt our coordinate system.
        ax = aLon*cosa - aLat*sina;
        ay = aLon*sina + aLat*cosa;
    }
}
