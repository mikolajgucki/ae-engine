package com.andcreations.chartboost;

import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Model.CBError;

import com.andcreations.ae.Log;

/**
 * Chartboost delegate.
 *
 * @author Mikolaj Gucki
 */
class AEChartboostDelegate extends ChartboostDelegate {
    /** The logcat tag.*/
    private static final String TAG = "SDL/AEChartboost";       
    
    /** */
    @Override
    public void didCacheInterstitial(String location) {
        Log.d(TAG,"AEChartboost.didCacheInterstitial(" + location + ")");
        AEChartboost.didCacheInterstitial(location);
    }
    
    /** */
    @Override
    public void didFailToLoadInterstitial(String location,
        CBError.CBImpressionError error) {
    //
        Log.d(TAG,"AEChartboost.didFailToLoadInterstitial(" + location +
            ", " + error.name() + ")");
        AEChartboost.didFailToLoadInterstitial(location);
    }
    
    /** */
    @Override
    public void didClickInterstitial(String location) {
        Log.d(TAG,"AEChartboost.didClickInterstitial(" + location + ")");
        AEChartboost.didClickInterstitial(location);
    }
    
    /** */
    @Override
    public void didCloseInterstitial(String location) {
        Log.d(TAG,"AEChartboost.didCloseInterstitial(" + location + ")");
        AEChartboost.didCloseInterstitial(location);
    }
    
    /** */
    @Override
    public void didDismissInterstitial(String location) {
        Log.d(TAG,"AEChartboost.didDismissInterstitial(" + location + ")");
        AEChartboost.didDismissInterstitial(location);
    }
    
    /** */
    @Override
    public void didDisplayInterstitial(String location) {
        Log.d(TAG,"AEChartboost.didDisplayInterstitial(" + location + ")");
        AEChartboost.didDisplayInterstitial(location);
    }    
}