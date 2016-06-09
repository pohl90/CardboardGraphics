package ba.pohl1.hm.edu.vrlibrary.physics.transitions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pohl on 01.05.2016.
 */
public class TransitionManager {

    private static final TransitionManager instance = new TransitionManager();

    private List<GLTransition> transitions = new ArrayList<>();

    private TransitionManager() {
        // Prevents instantiation
    }

    public static TransitionManager getInstance() {
        return instance;
    }

    public void executeTransitions() {
        for (final Iterator<GLTransition> iter = transitions.iterator(); iter.hasNext(); ) {
            final GLTransition transition = iter.next();
            transition.execute();
            if (transition.isDone()) {
                iter.remove();
            }
        }
    }

    public void addTransition(final GLTransition transition) {
        transitions.add(transition);
    }
}
