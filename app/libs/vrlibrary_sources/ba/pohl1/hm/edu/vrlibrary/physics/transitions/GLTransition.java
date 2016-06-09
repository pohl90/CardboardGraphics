package ba.pohl1.hm.edu.vrlibrary.physics.transitions;

/**
 * Created by Pohl on 22.04.2016.
 */
public abstract class GLTransition {

    protected int steps;

    public GLTransition(final float steps) {
        this.steps = (int) steps;
        TransitionManager.getInstance().addTransition(this);
    }

    public boolean isDone() {
        return steps <= 0;
    }

    /**
     * Executes the transition.
     *
     * @return {@code true} if the transition is done
     */
    public abstract boolean execute();
}
