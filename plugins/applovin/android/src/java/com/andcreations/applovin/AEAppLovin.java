package com.andcreations.applovin;

import android.app.Activity;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkSettings;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;

import com.andcreations.ae.Log;

/**
 * AppLovin integration.
 *
 * @author Mikolaj Gucki
 */
public class AEAppLovin {
    /** The logcat tag. */
    private static final String TAG = "AE/AppLovin";
    
    /** The activity. */
    private static Activity activity;
    
    /** The AppLovin SDK. */
    private static AppLovinSdk sdk;
    
    /** The cached interstitial ad. */
    private static AppLovinAd interstitialAd;
    
    /** The interstitial ad dialog. */
    private static AppLovinInterstitialAdDialog interstitialAdDialog;
    
    /** The interstitial ad load listener.*/
    private static AppLovinAdLoadListener interstitialAdLoadListener;
    
    /** The interstitial ad display listener. */
    private static AppLovinAdDisplayListener interstitialAdDisplayListener;
    
    /** The interstitial ad click listener. */
    private static AppLovinAdClickListener interstitialAdClickListener;
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();
    
    /**
     * Called when an interstitial ad has been loaded.
     */
    public static native void interstitialAdLoaded();
    
    /**
     * \brief Called when there are no interstital ads to display.
     */
    public static native void interstitialAdNoFill();
    
    /**
     * Called when loading of interstital ad fails.
     */
    public static native void interstitialAdFailed(int errorCode);
    
    /**
     * Called when an interstitial ad has been displayed.
     */
    public static native void interstitialAdDisplayed();
    
    /**
     * Called when an interstitial ad has been hidden.
     */
    public static native void interstitialAdHidden();
    
    /**
     * Called when an interstitial ad has been clicked.
     */
    public static native void interstitialAdClicked();
    
    /**
     * Initializes AppLovin. Must be called from onCreate().
     *
     * @param activity The activity.
     */
    public static void init(Activity activity) {
        AEAppLovin.activity = activity;
        createListeners();
        initSDK();
    }
    
    /** */
    private static void createListeners() {
    // load listener
        interstitialAdLoadListener = new AppLovinAdLoadListener() {
            /** */
            @Override
            public void adReceived(AppLovinAd ad) {
                Log.d(TAG,"adReceived()");
                interstitialAdLoaded();
            }
            
            /** */
            @Override
            public void failedToReceiveAd(int errorCode) {
                if (errorCode == AppLovinErrorCodes.NO_FILL) {
                    Log.d(TAG,"failedToReceiveAd(nofill)");                    
                    interstitialAdNoFill();
                }
                else {
                    Log.d(TAG,"failedToReceiveAd(" + errorCode + ")");
                    interstitialAdFailed(errorCode);
                }
            }
        };
        
    // display listener
        interstitialAdDisplayListener = new AppLovinAdDisplayListener() {
            /** */
            @Override
            public void adDisplayed(AppLovinAd ad) {
                Log.d(TAG,"adDisplayed()");
                interstitialAdDisplayed();
            }
            
            /** */
            @Override
            public void adHidden(AppLovinAd ad) {
                Log.d(TAG,"adHidden()");
                interstitialAdHidden();
            }
        };
        
    // click listener
        interstitialAdClickListener = new AppLovinAdClickListener() {
            /** */
            @Override
            public void adClicked(AppLovinAd ad) {
                Log.d(TAG,"adClicked()");
                interstitialAdClicked();
            }            
        };
    }    
    
    /**
     * Initializes the AppLovin SDK.
     */
    private static void initSDK() {
        Log.d(TAG,"initSDK()");
    // if already initialized
        if (sdk != null) {
            Log.d(TAG,"AppLovin already initialized");
            return;
        }        
        
    // initialize AppLovin
        AppLovinSdk.initializeSdk(activity);
        
    // SDK settings
        AppLovinSdkSettings settings = new AppLovinSdkSettings();
        settings.setVerboseLogging(true);
        sdk = AppLovinSdk.getInstance(settings,activity);        
    
    // listeners
        AppLovinInterstitialAdDialog adDialog =
            AppLovinInterstitialAd.create(sdk,activity);
        adDialog.setAdLoadListener(interstitialAdLoadListener);
        adDialog.setAdDisplayListener(interstitialAdDisplayListener);
        adDialog.setAdClickListener(interstitialAdClickListener);
    }   
    
    /**
     * Checks if an interstitial ad is ready for display.
     *
     * @return <code>true</code> is ready, <code>false</code> otherwise.
     */
    public static boolean isInterstitialAdReadyForDisplay() {
        Log.d(TAG,"isInterstitialAdReadyForDisplay()");
        return AppLovinInterstitialAd.isAdReadyToDisplay(activity);
    }
    
    /**
     * Shows the interstitial ad.
     */
    public static void showInterstitialAd() {
        Log.d(TAG,"showInterstitialAd()");
        activity.runOnUiThread(new Runnable() {
            /** */
            @Override
            public void run() {        
                Log.d(TAG,"showInterstitialAd() on UI thread");
                AppLovinInterstitialAd.show(activity);
            }
        });
    }
}
