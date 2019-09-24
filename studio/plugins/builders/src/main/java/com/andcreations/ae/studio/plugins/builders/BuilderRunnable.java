package com.andcreations.ae.studio.plugins.builders;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.andcreations.ae.studio.log.Log;
import com.andcreations.ae.studio.plugins.console.DefaultConsole;
import com.andcreations.ae.studio.plugins.ui.common.UICommon;
import com.andcreations.resources.BundleResources;

/**
 * @author Mikolaj Gucki
 */
class BuilderRunnable implements Runnable {
    /** */
    private static BuilderRunnable instance;
    
    /** */
    private BundleResources res = new BundleResources(BuilderRunnable.class);     
    
    /** Indicates if to stop the thread. */
    private boolean stopFlag;
    
    /** The queue of the builders to run. */
    private Queue<BuilderBatch> queue = new LinkedList<>();
    
    /** */
    private Thread thread;
    
    /** */
    private BuildersStatus status;
    
    /** */
    private Object currentBuilderLock = new Object();
    
    /** */
    private Builder currentBuilder;
    
    /** */
    private void build(Builder builder) {
    	long startTime = System.currentTimeMillis();
    			
	// build
        status.setAction(builder.getIcon(),builder.getName());
        builder.build();
        
    // log time
        long time = System.currentTimeMillis() - startTime;
        String timeStr = String.format("%.2f",time / 1000f);
        DefaultConsole.get().trace(String.format(
    		res.getStr("builder.finished",builder.getName(),timeStr)));
    }
    
    /**
     * Sets up the console according to the preferences.
     */
    private void setupConsole() {
    	UICommon.invokeAndWait(new Runnable() {
    		/** */
    		@Override
    		public void run() {
		        if (BuildersPreferences.get().getClearConsoleWhenBuilderStarts()) {
		        	DefaultConsole.get().clear();
		        }
		        if (BuildersPreferences.get().getShowConsoleWhenBuilderStarts()) {
		        	DefaultConsole.get().show();
		        }
    		}
    	});
    }
    
    /** */
    private void build(BuilderBatch batch) {
        Builders.get().setTerminable(false);
        setupConsole();
        
        List<Builder> builders = batch.getBuilders();
    // for each builder in the batch
        for (Builder builder:builders) {
            synchronized (currentBuilderLock) {
                currentBuilder = builder;
            }
            try {
                build(builder);
            } catch (BuilderException exception) {
                break;
            }
        }
        
        synchronized (currentBuilderLock) {
            currentBuilder = null;
        }
    }
    
    /** */
    @Override
    public void run() {
        while (stopFlag == false) {
        	BuilderBatch nextBatch = null;
            synchronized (queue) {
            // wait for the next builder if none at the moment
                if (queue.isEmpty() == true) {
                    status.setNoAction();
                    try {
                        queue.wait();
                    } catch (InterruptedException exception) {
                    }                    
                }

                if (stopFlag == true) {
                    break;
                }
                nextBatch = queue.poll();
            }
            build(nextBatch);            
        }
    }
    
    /** */
    void add(BuilderBatch batch) {
    	synchronized (queue) {
    		queue.add(batch);
    		queue.notify();
    	}
    }
    
    /** */
    void terminate() {
        synchronized (currentBuilderLock) {
            if (currentBuilder != null) {
                currentBuilder.terminate();
            }
        }
    }
    
    /** */
    static BuilderRunnable get() {
        if (instance == null) {
            instance = new BuilderRunnable();
        }
        
        return instance;
    }
    
    /** */
    void init(BuildersStatus status) {
        this.status = status;
    }
    
    /** */
    void start() {
        Log.info("Starting the builder thread");
        thread = new Thread(this,"Builder");
        thread.start();
    }
    
    /** */
    void stop() {
        synchronized (queue) {
            stopFlag = true;
            queue.notify();
        }
        
        try {
            thread.join();
        } catch (InterruptedException exception) {
        }
        Log.info("Stopped the builder thread");
    }
}