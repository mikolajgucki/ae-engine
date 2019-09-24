package com.andcreations.vungle;

import com.vungle.publisher.EventListener;

import com.andcreations.ae.Log;

/**
 * The Vungle listener.
 *
 * @author Mikolaj Gucki
 */
public class AEVungleListener implements EventListener {
    /** The logcat tag. */
    private static final String TAG = "SDL/AEVungle";    
    
    /** */
    @Override
    public void onAdPlayableChanged(boolean isAdPlayable) {
        Log.d(TAG,"AEVungleListener.onAdPlayableChanged(" + isAdPlayable + ")");
        AEVungle.onAdPlayableChanged(isAdPlayable);
    }
    
    /** */
    @Override
    public void onAdUnavailable(String reason) {
        Log.d(TAG,"AEVungleListener.onAdUnavailable(" + reason + ")");
    }
    
    /** */
    @Override
    public void onAdStart() {
        Log.d(TAG,"AEVungleListener.onAdStart()");
        AEVungle.willShowAd();
    }
    
    /** */
    @Override
    public void onAdEnd(boolean wasSuccessfulView,boolean wasCallToActionClicked) {
        Log.d(TAG,"AEVungleListener.onAdEnd(" + wasSuccessfulView + "," +
            wasCallToActionClicked + ")");
        AEVungle.willCloseAd();
        
        if (wasSuccessfulView == true) {
            AEVungle.viewCompleted();
        }
        if (wasCallToActionClicked == true) {
            AEVungle.adClicked();
        }
    }
    
    /** */
    @Override    
    public void onVideoView(boolean isCompletedView,int watchedMillis,
        int videoMillis) {
    // nothing here as this method is deprecated
        Log.d(TAG,"AEVungleListener.onVideoView(" + isCompletedView + ")");
    }
}