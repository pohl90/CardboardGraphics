package ba.pohl1.hm.edu.vrlibrary.cardboard;

/**
 * A listener to retrieve cardboard trigger notifications.
 *
 * Created by Pohl on 01.05.2016.
 */
public interface CardboardTriggerListener {

    /**
     * Notifies the listener that the cardboard trigger is used.
     *
     * @param event the {@link CardboardTriggerEvent}
     */
    void onCardboardTrigger(final CardboardTriggerEvent event);
}
