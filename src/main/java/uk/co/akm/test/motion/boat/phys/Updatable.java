package uk.co.akm.test.motion.boat.phys;

/**
 * All implementations of this interface have a state that can be updated over a small time increment.
 *
 * Created by Thanos Mavroidis on 14/01/2014.
 */
public interface Updatable {

    /**
     * Updates the body state over a small time increment.
     *
     * @param dt the small time increment
     */
    void update(double dt);
}
