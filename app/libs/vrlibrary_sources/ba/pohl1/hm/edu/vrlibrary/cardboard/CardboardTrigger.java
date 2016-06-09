package ba.pohl1.hm.edu.vrlibrary.cardboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the trigger specified by the Google Cardboard SDK.
 *
 * Created by Pohl on 01.05.2016.
 */
public class CardboardTrigger {

    private final List<CardboardTriggerListener> listeners = new ArrayList<>();

    /**
     * Executes the cardboard trigger and notifies all {@link CardboardTriggerListener}s.
     * Once a trigger event has been consumed the trigger notification stops.
     *
     * @return {@code true} if the event was not consumed
     */
    public boolean onCardboardTrigger() {
        final CardboardTriggerEvent event = new CardboardTriggerEvent();
        for (CardboardTriggerListener listener : listeners) {
            listener.onCardboardTrigger(event);
            if (event.isConsumed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a {@link CardboardTriggerListener}.
     *
     * @param listener the listener to add
     */
    public void addListener(final CardboardTriggerListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given {@link CardboardTriggerListener}.
     *
     * @param listener the listener to remove
     */
    public void removeListener(final CardboardTriggerListener listener) {
        listeners.remove(listener);
    }

    /**
     * Disposes the cardboard trigger
     */
    public void dispose() {
        listeners.clear();
    }
}
