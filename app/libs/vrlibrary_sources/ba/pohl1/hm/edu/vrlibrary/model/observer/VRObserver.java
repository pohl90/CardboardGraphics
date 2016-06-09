package ba.pohl1.hm.edu.vrlibrary.model.observer;

/**
 * A listener which is notified when a {@link VRObservable} changed.
 *
 * Created by Pohl on 21.04.2016.
 */
public interface VRObserver<T> {

    /**
     * Notifies this observer when a {@link VRObservable} has changed.
     *
     * @param oldValue the old value of the observable
     * @param newValue the new value of the observable
     */
    void onChange(final T oldValue, final T newValue);

}
