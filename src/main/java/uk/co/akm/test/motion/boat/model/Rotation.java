package uk.co.akm.test.motion.boat.model;

/**
 * Direction of rotation (turn).
 *
 * Created by Thanos Mavroidis on 03/02/2018.
 */
public enum Rotation {
    LEFT(1), NONE(0), RIGHT(-1);

    public final int sign;

    Rotation(int sign) {
        this.sign = sign;
    }
}
