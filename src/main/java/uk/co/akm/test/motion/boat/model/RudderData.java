package uk.co.akm.test.motion.boat.model;

/**
 * Boat structure and turning target performance parameters that can be used to estimate the parameters that govern the
 * boat turning behaviour. In particular, these parameters can be used to estimate the deflection force exerted by the
 * rudder as well as the turning resistance torque to the angular motion initiated by the rudder deflection force.
 *
 * Created by Thanos Mavroidis on 26/01/2018.
 */
public final class RudderData {
    /**
     * The length of the boat.
     */
    public final double length;

    /**
     * The distance of the boat centre of gravity from the stern.
     */
    public final double cogDistanceFromStern;

    /**
     * The constant boat angular velocity during a sustained turn at a constant boat speed.
     */
    public final double omega;

    /**
     * The constant boat speed during a sustained turn with a constant angular velocity.
     */
    public final double v;

    /**
     * The speed limit where the resistance force transitions from being proportional to the square of the boat speed
     * to simply being proportional to the boat speed itself.
     */
    public final double vTransition;

    public RudderData(double length, double cogDistanceFromStern, double omega, double v, double vTransition) {
        checkArgs(length, cogDistanceFromStern, omega, v, vTransition);

        this.length = length;
        this.cogDistanceFromStern = cogDistanceFromStern;
        this.omega = omega;
        this.v = v;
        this.vTransition = vTransition;
    }

    private void checkArgs(double length, double cogDistanceFromStern, double omega, double v, double vTransition) {
        if (cogDistanceFromStern >= length) {
            throw new IllegalArgumentException("Distance of the centre of gravity from the stern (" + cogDistanceFromStern + ") is more than the total boat length (" + length + ").");
        }

        if (v < vTransition) {
            throw new IllegalArgumentException("Input boat turning speed (" + v + ") is too low. It is less than the transition speed (" + vTransition + ").");
        }

        final double omegaMin = Math.min(2*vTransition/cogDistanceFromStern, 2*vTransition/(length - cogDistanceFromStern));
        if (omega < omegaMin) {
            throw new IllegalArgumentException("Input boat turning angular velocity (" + omega + ") is less that the minimum value (" + omegaMin + ").");
        }
    }
}
