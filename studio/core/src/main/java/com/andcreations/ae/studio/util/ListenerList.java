package com.andcreations.ae.studio.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Thread-safe and concurrent-safe listener list.
 *
 * @author Mikolaj Gucki
 */
public class ListenerList<T> implements Iterable<T> {
    /** */
    private List<T> listeners = new ArrayList<>();
    
    private boolean iterating;
    
    /** */
    public synchronized void add(final T listener) {
        if (iterating == true) {
            Thread thread = new Thread(new Runnable() {
                /** */
                @Override
                public void run() {
                    listeners.add(listener);
                }
            });
            thread.start();
        }
        else {
            listeners.add(listener);
        }
    }
    
    /** */
    public synchronized void remove(final T listener) {
        if (iterating == true) {
            Thread thread = new Thread(new Runnable() {
                /** */
                @Override
                public void run() {
                    listeners.remove(listener);
                }
            });
            thread.start();
        }
        else {
            listeners.remove(listener);
        }
    }    
    
    /** */
    public Iterator<T> iterator() {
        return listeners.iterator();
    }
    
    /** */
    public void iterationStart() {
        iterating = true;
    }
    
    /** */
    public void iterationEnd() {
        iterating = false;
    }
}