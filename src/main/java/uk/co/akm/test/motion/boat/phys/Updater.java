package uk.co.akm.test.motion.boat.phys;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public class Updater {

    public static void update(Updatable updatable, double time, int nSteps) {
        final double dt = time/nSteps;
        for (int i=0 ; i<nSteps ; i++) {
            updatable.update(dt);
        }
    }

    private Updater() {}
}
