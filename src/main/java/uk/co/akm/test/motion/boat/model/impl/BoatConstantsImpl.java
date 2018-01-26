package uk.co.akm.test.motion.boat.model.impl;

import uk.co.akm.test.motion.boat.model.BoatConstants;
import uk.co.akm.test.motion.boat.model.RudderData;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatConstantsImpl implements BoatConstants {
    private final double kLon;
    private final double kLat;
    private final double kLonReverse;
    private final double kRud;
    private final RudderData rudderData;

    public BoatConstantsImpl(double kLon, double kLatOverKLon, double kLonReverseOverKLon) {
        this.kLon = kLon;
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
        this.kRud = 0;
        this.rudderData = null;
    }

    public BoatConstantsImpl(double kLon, double kLatOverKLon, double kLonReverseOverKLon, RudderData rudderData) {
        this.kLon = kLon;
        this.kLat = kLon*kLatOverKLon;
        this.kLonReverse = kLon*kLonReverseOverKLon;
        this.rudderData = rudderData;
        this.kRud = kRudEstimation(kLat, rudderData);
    }

    private double kRudEstimation(double kLat, RudderData rudderData) {
        final double cogDistanceFromBow = (rudderData.length - rudderData.cogDistanceFromStern);
        final double omegaSq = rudderData.omega * rudderData.omega;
        final double vSq = rudderData.v * rudderData.v;

        return (kLat/(8*rudderData.length)) * Math.pow(rudderData.cogDistanceFromStern, 3) * Math.pow(cogDistanceFromBow, 4) * omegaSq/vSq;
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

    @Override
    public double kRud() {
        return kRud;
    }

    @Override
    public RudderData getRudderData() {
        return rudderData;
    }
}
