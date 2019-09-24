package com.andcreations.gpgs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentSender;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;

import com.andcreations.ae.Log;

/**
 * The main class for Google Play Game Services support.
 *
 * @author Mikolaj Gucki    
 */
public class AEGPGS {
    /** The log tag. */
    private static final String TAG = "AE/GPGS";
    
    /** The request code for sign-in. */
    private static int RC_SIGN_IN = 9001;
    
    /** The request code for displaying achievements. */
    private static int RC_ACHIEVEMENTS = 9002;
    
    /** The request code for displaying a leaderboard. */
    private static int RC_LEADERBOARD = 9003;
    
    /** The activity. */
    private static Activity activity;
    
    /** The view for popups. */
    private static View viewForPopups;
    
    /** The API client. */
    private static GoogleApiClient googleApiClient;
    
    /** Indicates if the sign-in button has been clicked/tapped. */
    private static boolean signingIn;    
    
    /** Indicates if we're already resolving the connection failure. */
    private static boolean resolvingConnectionFailure;
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();
    
    /** */
    public static native void signedIn();
    
    /** */
    public static native void signedOut();
    
    /**
     * Initializes Google Play Game Services.
     *
     * @param activity The activity.
     */
    public static void onCreate(Activity activity,View viewForPopups) {
        AEGPGS.activity = activity;
        AEGPGS.viewForPopups = viewForPopups;
        
        ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
            /** */
            public void onConnected(Bundle bundle) {
                AEGPGS.onConnected(bundle);
            }
            
            /** */
            public void onConnectionSuspended(int cause) {
                AEGPGS.onConnectionSuspended(cause);
            }
        };
        
        OnConnectionFailedListener failedListener =
            new OnConnectionFailedListener() {
            /** */
            public void onConnectionFailed(ConnectionResult result) {
                AEGPGS.onConnectionFailed(result);
            }
        };
        
    // initial values    
        signingIn = false;
        resolvingConnectionFailure = false;
        
    // Create the Google Api Client with access to the Play Game services
        googleApiClient = new GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(connectionCallbacks)
            .addOnConnectionFailedListener(failedListener)
            .addApi(Games.API).addScope(Games.SCOPE_GAMES)
            .build();       
    }
    
    /** */
    private static void onConnected(Bundle bundle) {
        Log.d(TAG,"onConnected()");
        Games.setViewForPopups(googleApiClient,viewForPopups);        
        signedIn();
    }
    
    /** */
    private static void onConnectionSuspended(int cause) {
        String causeStr = "Unknown cause";
        if (cause == ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            causeStr = "Network lost";
        }
        if (cause == ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            causeStr = "Service disconnected";
        }        
        Log.d(TAG,"Connection suspended due to " + causeStr);
        
    // Attempt to reconnect
        googleApiClient.connect();
    }    
    
    /** */
    public static void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
            .setMessage(message)
            .setNeutralButton(android.R.string.ok,null)
            .create()
            .show();
    }    
    
    /** */
    private static boolean resolveConnectionFailure(ConnectionResult result,
        int requestCode,String fallbackErrorMessage) {
    //
        Log.d(TAG,"resolveConnectionFailure()");
    
        if (result.hasResolution() == true) {
            try {
                result.startResolutionForResult(activity,requestCode);
                return true;
            } catch (IntentSender.SendIntentException exception) {
                // The intent was canceled before it was sent. Return to the
                // default state and attempt to connect to get an updated
                // ConnectionResult.
                googleApiClient.connect();
                return false;
            }
        } else {
        // not resolvable... so show an error message
            int errorCode = result.getErrorCode();
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,activity,requestCode);
            if (dialog != null) {
                dialog.show();
            } else {
                // no built-in dialog: show the fallback error message
                showAlert(fallbackErrorMessage);
            }
            
            return false;
        }
    }     
    
    /** */
    private static void showActivityResultError(int requestCode,int response,
        String fallbackErrorMessage) {
    //
        switch (response) {
            case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
                showAlert("Game Services Error: Application is misconfigured");
                break;
            case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
                showAlert("Game Services Error: Sign-in failed");
                break;
            case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
                showAlert("Game Services Error: License error");
                break;
            default:
                int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(
                    activity);
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(code,
                    activity,requestCode,null);
                if (dialog != null) {
                    dialog.show();
                }
                else {
                    showAlert(fallbackErrorMessage);
                }
        }
    }
    
    /** */
    private static void onConnectionFailed(ConnectionResult result) {
        Log.d(TAG,"onConnectionFailed(" + result.getErrorCode() + ")");
        
        if (resolvingConnectionFailure == true) {
            return;
        }
        
        if (signingIn == true) {
            signingIn = false;
            resolvingConnectionFailure = true;            
            
        // TODO Localize error message.
            String msg = "There was an issue with sign in to Game " +
                "Services. Please try again later.";                
                
            if (resolveConnectionFailure(result,RC_SIGN_IN,msg) == true) {
                resolvingConnectionFailure = false;
            }
        }
    }
    
    /** */
    public static void onStart() {
        googleApiClient.connect();
    }
    
    /** */
    public static void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
    
    /** */
    public static boolean onActivityResult(int requestCode,int resultCode,
        Intent intent) {
    // sign-in
        if (requestCode == RC_SIGN_IN) {
            signingIn = false;
            resolvingConnectionFailure = false;
            
            if (resultCode == Activity.RESULT_OK) {
                googleApiClient.connect();
            }
            else {
            // TODO Localize error message.
                String msg = "There was an issue with sign in to Game " +
                    "Services. Please try again later.";                
                showActivityResultError(requestCode,resultCode,msg);
            }
            
            return true;
        }
    
        return false;
    }
    
    /** */
    public static void signIn() {
        signingIn = true;        
        googleApiClient.connect();
    }
    
    /** */
    public static void signOut() {
        signingIn = false;
        Games.signOut(googleApiClient);
        googleApiClient.disconnect();
        signedOut();
    }
    
    /** */
    public static boolean isSignedIn() {
        boolean signedIn =  googleApiClient != null &&
            googleApiClient.isConnected();
        return signedIn;
    }
    
    /** */
    public static void unlockAchievement(String id) {
        if (isSignedIn() == true) {
            Games.Achievements.unlock(googleApiClient,id);
        }
    }
    
    /** */
    public static void incrementAchievement(String id,int steps) {
        if (isSignedIn() == true) {
            Games.Achievements.increment(googleApiClient,id,steps);
        }
    }
    
    /** */
    public static void setAchievementSteps(String id,int steps) {
        if (isSignedIn() == true) {
            Games.Achievements.setSteps(googleApiClient,id,steps);
        }        
    }
    
    /** */
    public static void submitScore(String leaderboardId,long score) {
        if (isSignedIn() == true) {
            Log.d(TAG,"submitScore(" + leaderboardId + "," + score + ")");
            Games.Leaderboards.submitScore(googleApiClient,leaderboardId,score);
        } 
    }
    
    /** */
    public static void displayAchievements() {
        System.out.println("displayAchievements(100)");
        
        //if (isSignedIn() == true) {
            Log.d(TAG,"About to display achievements");
            activity.startActivityForResult(
                Games.Achievements.getAchievementsIntent(googleApiClient),
                RC_ACHIEVEMENTS);
        //}
        System.out.println("displayAchievements(200)");
    }
    
    
    /** */
    public static void displayLeaderboard(String leaderboardId) {
        System.out.println("displayLeaderboard(100)");        
        Log.d(TAG,"displayLeaderboard(" + leaderboardId + ")");
        System.out.println("displayLeaderboard(101)");        
        
        //if (isSignedIn() == true) {        
            Log.d(TAG,"About to display leaderboard");
            activity.startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(googleApiClient,
                leaderboardId),RC_LEADERBOARD);
        //}
        System.out.println("displayLeaderboard(200)");        
    }    
}