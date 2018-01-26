package uk.co.akm.test.motion.boat.model;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public interface BoatConstants {

    /**
     * @return the longitudinal motion resistance coefficient (i.e. along the boat axis).
     */
    double kLon();

    /**
     * @return the lateral motion resistance coefficient (i.e. perpendicular to the boat axis).
     */
    double kLat();

    /**
     * @return the longitudinal motion resistance coefficient in the reverse direction (i.e. when the boat is reversing along its axis).
     */
    double kLonReverse();

    /**
     * @return the coefficient that when multiplied by the square of the boat longitudinal speed yields the rudder deflection force.
     */
    double kRud();

    /**
     * @return the rudder data used to estimate the resistance torque to the boat angular motion.
     */
    RudderData getRudderData();
}
