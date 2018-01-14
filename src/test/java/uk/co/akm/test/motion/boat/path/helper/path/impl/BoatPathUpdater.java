package uk.co.akm.test.motion.boat.path.helper.path.impl;

import uk.co.akm.test.motion.boat.path.helper.path.BoatPath;
import uk.co.akm.test.motion.boat.phys.State;
import uk.co.akm.test.motion.boat.phys.UpdatableState;
import uk.co.akm.test.motion.boat.phys.Updater;

/**
 * Created by Thanos Mavroidis on 14/01/2018.
 */
public final class BoatPathUpdater {
    private final int nSteps;

    private final int nViewSteps;
    private final BoatPath path;
    private final Updater.UpdateView updateView;

    public BoatPathUpdater(int nSteps, int nPoints) {
        this.nSteps = nSteps;

        this.nViewSteps = nSteps/nPoints;
        this.path = new BoatPathImpl(nPoints);
        this.updateView = new BoatPathUpdateView(path);
    }

    public void update(double time, UpdatableState state) {
        Updater.update(state, time, nSteps, updateView, nViewSteps);
    }

    public BoatPath getPath() {
        return path;
    }

    private static final class BoatPathUpdateView implements Updater.UpdateView {
        private final BoatPath path;

        BoatPathUpdateView(BoatPath path) {
            this.path = path;
        }

        @Override
        public void view(State state) {
            path.addPoint(state);
        }
    }
}
