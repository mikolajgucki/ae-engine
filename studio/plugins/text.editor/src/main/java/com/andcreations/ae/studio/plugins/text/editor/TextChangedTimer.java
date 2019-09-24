package com.andcreations.ae.studio.plugins.text.editor;

/**
 * @author Mikolaj Gucki
 */
class TextChangedTimer implements Runnable {
    /** */
    private static final long DURATION = 1000;
    
    /** */
    private TextChangedTimerListener listener;
    
    /** */
    private EditorMediator mediator;
    
    /** */
    private long startTime;
    
    /** */
    private boolean stopFlag;
    
    /** */
    TextChangedTimer(TextChangedTimerListener listener) {
        this.listener = listener;
    }
    
    /** */
    @Override
    public void run() {
        while (stopFlag == false) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException exception) {
                    stopFlag = true;
                }
            }
            if (stopFlag == true) {
                break;
            }
            
            while (true) {
                if (stopFlag == true) {
                    break;
                }
                
                synchronized (this) {
                    if (mediator == null) {
                        break;
                    }
                    long timeDelta = System.currentTimeMillis() - startTime;
                    if (timeDelta >= DURATION) {
                        notifyTextChanged(mediator);
                        mediator = null;
                        break;
                    }
                }
            }
        }
    }
    
    /** */
    private void notifyTextChanged(final EditorMediator mediator) {
        Runnable notifier = new Runnable() {
            /** */
            @Override
            public void run() {
                listener.textChangedTimerFired(mediator);
            }
        };
        
        Thread thread = new Thread(notifier,"TextEditorChangedNotifier");
        thread.start();
    }
    
    /** */
    synchronized void startCounting(EditorMediator mediator) {
        this.mediator = mediator;
        this.startTime = System.currentTimeMillis();
        notify();
    }
    
    /** */
    synchronized void stopCounting() {
        mediator = null;
    }
    
    /** */
    synchronized void stop() {
        stopFlag = true;
        notify();
    }
}