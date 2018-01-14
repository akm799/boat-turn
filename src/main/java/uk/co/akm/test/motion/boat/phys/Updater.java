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

    public static void update(UpdatableState updatable, double time, int nSteps, UpdateView updateView, int nViewSteps) {
        final double dt = time/nSteps;
        for (int i=0 ; i<nSteps ; i++) {
            updatable.update(dt);

            if ((i + 1)%nViewSteps == 0) {
                updateView.view(updatable);
            }
        }
    }

    private Updater() {}

    public interface UpdateView {
        void view(State state);
    }
}
