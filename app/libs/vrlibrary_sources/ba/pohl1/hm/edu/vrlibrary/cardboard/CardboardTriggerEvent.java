package ba.pohl1.hm.edu.vrlibrary.cardboard;

/**
 * A simple data structure to hold information about consumption.
 *
 * Created by Pohl on 01.05.2016.
 * @see {@link CardboardTrigger#onCardboardTrigger()} for more information about event consumption
 */
public class CardboardTriggerEvent {

    private boolean consumed;

    /**
     * Consumes the trigger event.
     */
    public void consume() {
        consumed = true;
    }

    /**
     * Gets whether the event is consumed.
     *
     * @return {@code true} if the event is consumed
     */
    public boolean isConsumed() {
        return consumed;
    }
}
