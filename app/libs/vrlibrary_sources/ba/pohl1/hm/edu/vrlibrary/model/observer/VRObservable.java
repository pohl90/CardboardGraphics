package ba.pohl1.hm.edu.vrlibrary.model.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A template model class that can be observed by {@link VRObserver}.
 *
 * Created by Pohl on 21.04.2016.
 */
public class VRObservable<T> {

    private List<VRObserver> observers = new ArrayList<>();
    private T value;

    public VRObservable() {
        this(null);
    }

    public VRObservable(final T value) {
        this.value = value;
    }

    public void dispose() {
        observers.clear();
    }

    /**
     * Gets whether there is non null value currently set.
     *
     * @return {@code true} if there is a non null value
     */
    public boolean isPresent() {
        return get() != null;
    }

    /**
     * Sets the new value and notifes all listeners.
     *
     * @param value the value to set
     */
    public void set(final T value) {
        onChange(this.value, this.value = value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public T get() {
        return value;
    }

    /**
     * Adds a {@link VRObserver} to this model.
     *
     * @param listener the listener to add
     */
    public void addListener(final VRObserver<T> listener) {
        observers.add(listener);
    }

    /**
     * Removes a {@link VRObserver} from this model.
     *
     * @param listener the listener to remove
     */
    public void removeListener(final VRObserver<T> listener) {
        observers.remove(listener);
    }

    private void onChange(final T oldValue, final T newValue) {
        if(Objects.equals(oldValue, newValue)) {
            return;
        }
        for(final VRObserver observer : observers) {
            observer.onChange(oldValue, newValue);
        }
    }
}
