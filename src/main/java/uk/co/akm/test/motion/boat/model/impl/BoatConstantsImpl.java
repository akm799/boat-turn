package uk.co.akm.test.motion.boat.model.impl;

import uk.co.akm.test.motion.boat.model.BoatConstants;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatConstantsImpl implements BoatConstants {
    private final double kLon;
    private final double kLat;
    private final double kLonReverse;

    public BoatConstantsImpl(double kLon, double kLatOverKLon, double kLonReverseOverKLon) {
        this.kLon = kLon;
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
    }

    @Override
    public double kLon() {
        return kLon;
    }

    @Override
    public double kLat() {
        return kLat;
    }

    @Override
    public double kLonReverse() {
        return kLonReverse;
    }
}
