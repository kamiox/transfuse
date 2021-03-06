package org.androidtransfuse.event;

import java.lang.ref.WeakReference;

/**
 * Weak referencing Event Observer. Will trigger an event on the given reference only if the referenced
 * object exists.
 *
 * @author John Ericksen
 */
public abstract class WeakObserver<E, T> implements EventObserver<E>{

    private final WeakReference<T> reference;

    public WeakObserver(T target){
        reference = new WeakReference<T>(target);
    }

    @Override
    public void trigger(E event) {
        T handle = reference.get();
        if(handle != null){
            trigger(event, handle);
        }
    }

    public abstract void trigger(E event, T handle);
}
