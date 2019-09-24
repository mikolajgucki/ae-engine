package com.andcreations.iap;

import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;

import com.andcreations.iab.IABResponseCode;
import com.andcreations.iab.IABProduct;
import com.andcreations.iab.IABPurchase;
import com.andcreations.iab.IABListener;
import com.andcreations.iab.IABHelper;

import com.andcreations.ae.Log;

/**
 * The main class for in-app purchases support.
 *
 * @author Mikolaj Gucki
 */
public class IAP {
    /** The logcat tag. */
    private static final String TAG = "AE/IAP";

    /** The in-app billing request code. */
    private static final int IAB_REQUEST_CODE = 1001;    
    
    /** The configuration. */
    private static IAPCfg cfg;
    
    /** The in-app billing. */
    private static IABHelper iab;

    /** Indicates if IAP has been initialized. */
    private static boolean initialized;
    
    /** The start failure message. */
    private static String startFailure;    
    
    /** Indicates the init() method has been called. */
    private static boolean initCalled;
    
    /** Indicates if the Lua library start function has already been called. */
    private static boolean libLibStartCalled;
    
    /**
     * Loads the Lua library.
     */
    public static native void loadLuaLib();    
    
    /** */
    public static native void started(); 
    
    /** */
    public static native void startFailed();
    
    /** */
    public static native void purchasing(String productId);
    
    /** */
    public static native void purchased(String productId);
    
    /** */
    public static native void purchaseFailed(String reason);
    
    /** */
    public static native void alreadyOwned(String productId);
    
    /** */
    public static native void purchaseCanceled();
    
    /** */
    public static native void restorePurchasesFailed(String reason);
    
    /** */
    public static native void purchasesRestored();
    
    /**
     * Gets a product by identifier.
     *
     * @param id The product identifier.
     * @return The product or <code>null</code> if there is no such identifier.
     */
    private static IAPProduct getProduct(String id) {
        for (IAPProduct product:cfg.getProducts()) {
            if (product.getId().equals(id)) {
                return product;
            }            
        }
        
        return null;
    }
    
    /**
     * Initializes in-app purchases.
     *
     * @param activity The main application activity.
     * @param publicKey The application public key.
     * @param cfg The configuration.
     */
    public static void init(Activity activity,String publicKey,IAPCfg cfg) {
        Log.d(TAG,"IAP.init(...)");
        IAP.cfg = cfg;
        
        IAP.initialized = false;
        IAP.startFailure = null;
        IAP.initCalled = false;
        IAP.libLibStartCalled = false;
    
    // create listener
        IABListener listener = new IABListener() {
            /** */
            @Override
            public void iabFailedToBind() {
                Log.d(TAG,"IAP.iabFailedToBind()");
                startFailure = "Failed to bind to service";
            }
            
            /** */
            @Override
            public void iabConnected() {
                synchronized (IAP.class) {
                    Log.d(TAG,"IAP.iabConnected()");
                    connected();
                }
            }
            
            /** */
            @Override
            public void iabDisconnected() {
                Log.d(TAG,"IAP.iabDisconnected()");
            }
            
            /** */
            @Override
            public void iabPurchaseFailed(String message) {
                Log.d(TAG,"IAP.iabPurchaseFailed(" + message + ")");
                purchaseFailed(message);
            }
            
            /** */
            @Override
            public void iabPurchaseFailed(IABResponseCode responseCode) {
                Log.d(TAG,"IAP.iabPurchaseFailed(" +
                    responseCode.toString() + ")");
                purchaseFailed(responseCode.toString());
            }
            
            /** */
            @Override
            public void iabPurchaseCanceled() {
                Log.d(TAG,"IAP.iabPurchaseCanceled()");
                purchaseCanceled();
            }

            /** */
            @Override
            public void iabPurchased(IABPurchase purchase) {
                Log.d(TAG,"IAP.iabPurchased()");
                purchased(purchase);
            }
        };
        
    // bind
        iab = new IABHelper(activity,listener,publicKey);
    }

    /**
     * Called when the IAP has been initialized.
     */
    private static void initialized() {
        initialized = true;
        callLuaLibStart();
    }
    
    /**
     * Called when initialization has failed.
     *
     * @param reason The failure reason.
     */
    private static void initFailed(String reason) {
        startFailure = reason;
        callLuaLibStart();
    }
    
    /**
     * Checks if the Lua library start function can be called. 
     */
    private static boolean canCallLuaLibStart() {
    // if already called
        if (libLibStartCalled == true) {
            return false;
        }
        
    // can call if purchases have been initially processed or it failed to
    // process the purchases
        return initialized == true || startFailure != null;
    }
    
    /** */
    private static void callLuaLibStart() {
        if (canCallLuaLibStart() == false) {
            return;
        }
        
    // If init() hasn't been yet called from the Lua library then it means that
    // the library is not yet loaded and no functions can be called. Pushing
    // the start is delayed until the library is loaded and initialized.
        if (initCalled == false) {
            return;
        }  
        
    // if not initialized and no failure set, then cannot yet be started        
        if (initialized == false && startFailure == null) {
            return;
        }
        
        if (startFailure == null) {
            started();
        }
        else {
            startFailed();
        }
        
        libLibStartCalled = true;
    }
    
    /**
     * Called right after it's connected to the service.
     */
    private static void connected() {
        if (consumePurchases() == false) {
            return;
        }
        if (cfg.getRetrieveProductDetails() == true) {
            if (getProductDetails() == false) {
                return;
            }
        }
        else {
            Log.d(TAG,"Skipping getting product details");
        }
        
        initialized();
    }
    
    /**
     * Consumes the consumable purchases.
     *
     * @return <code>true</code> on success, <code>false</code> otherwise.     
     */
    public static boolean consumePurchases() {
        Log.d(TAG,"IAP.consumePurchases()");
        
    // do nothing if not yet connected
        if (iab.isConnected() == false) {
            Log.d(TAG,"Not yet connected. Skipping consuming purchases.");
            return true;
        }
        
    // get all the purchases
        List<IABPurchase> purchases = new ArrayList<IABPurchase>();
        try {
            IABResponseCode code = iab.getPurchases(purchases);
            if (code != IABResponseCode.OK) {
                String err = "Error getting purchases. Response is " + code;
                Log.e(TAG,err);
                initFailed(err);
                return false;
            }
        } catch (RemoteException exception) {
            Log.e(TAG,"Error getting purchases",exception);
            initFailed("Error getting purchases: " + exception.getMessage());
            return false;
        }
        
    // for each purchase
        for (IABPurchase purchase:purchases) {
        // get the product
            IAPProduct product = getProduct(purchase.getProductId());
            if (product == null) {
                String err = "Got purchase of an unknown product " +
                    purchase.getProductId();
                Log.e(TAG,err);
                initFailed(err);
                return false;
            }
            
        // consume the purchase if the product is consumable
            if (product.isConsumable()) {
                try {
                    iab.consumePurchase(purchase.getPurchaseToken());
                } catch (RemoteException exception) {
                    Log.e(TAG,"Error consuming purchase",exception);
                    initFailed("Error consuming purchase: " +
                        exception.getMessage());
                    return false;
                }
            }
        }

        return true;
    }
    
    /**
     * Gets details on the products.
     *
     * @return <code>true</code> on success, <code>false</code> otherwise.
     */
    private static boolean getProductDetails() {
        Log.d(TAG,"IAP.getProductDetails()");
        
    // do nothing if not yet connected
        if (iab.isConnected() == false) {
            Log.d(TAG,"Not yet connected. Skipping getting product details.");
            return true;
        }        
        
    // identifier list
        List<String> idList = new ArrayList<String>();
        for (IAPProduct product:cfg.getProducts()) {
            idList.add(product.getId());
        }
        
        List<IABProduct> iabProducts = new ArrayList<IABProduct>();
    // query
        try {
            IABResponseCode code = iab.getSKUDetails(idList,iabProducts);
            if (code != IABResponseCode.OK) {
                String err = "Error querying products. Response is " + code;
                Log.e(TAG,err);
                initFailed(err);
                return false;
            }
        } catch (RemoteException exception) {
            Log.e(TAG,"Error querying products",exception);
            initFailed("Error querying products: " +
                exception.getMessage());
            return false;            
        }
        
    // set the IAB products
        for (IABProduct iabProduct:iabProducts) {
            IAPProduct product = getProduct(iabProduct.getId());
            if (product == null) {
                Log.e(TAG,"Received unknown product " + iabProduct.getId());
                continue;
            }
            
            product.setIABProduct(iabProduct);
        }
        
    // check if all IAP products have IAB products
        for (IAPProduct product:cfg.getProducts()) {
            if (product.getIABProduct() == null) {
                Log.e(TAG,"Did not receive product " + product.getId()); 
            }
        }
        
        return true;
    }
    
    /**
     * Initializes the Lua library (called from the Lua library).
     */
    public static void init() {
        synchronized (IAP.class) {
            Log.d(TAG,"IAP.init()");
            initCalled = true;
            callLuaLibStart();
        }
    }
    
    /**
     * Checks if IAP is supported (called from the Lua library).
     *
     * @return <code>true</code> if supported, <code>false</code> otherwise.
     */
    public static boolean isSupported() {
        Log.d(TAG,"IAP.isSupported()");
        
        boolean supported = false;
        try {
            IABResponseCode code = iab.isSupported();
            Log.d(TAG,"  Code is " + code);
            if (code == IABResponseCode.OK) {
                supported = true;
            }    
        } catch (RemoteException exception) {
            Log.e(TAG,"Error checking if IAB is supported",exception);
        }
        
        Log.d(TAG,"IAB supported: " + supported);
        return supported;
    }
    
    /**
     * Gets the IAB products (called from the Lua library).
     *
     * @return The IAB products.
     */
    public static List<IABProduct> getIABProducts() {
        List<IABProduct> iabProducts = new ArrayList<IABProduct>();
    // for each product
        for (IAPProduct product:cfg.getProducts()) {
            if (product.getIABProduct() != null) {
                iabProducts.add(product.getIABProduct());
            }
            else {
                iabProducts.add(new IABProduct(product.getId(),"?","?",-1,"?"));
            }
        }
        
        return iabProducts;
    }

    /**
     * Purchases a product (called from the Lua library).
     *
     * @param productId The product identifier.
     */
    public static void purchase(String productId) {
        Log.d(TAG,"IAP.purchase(" + productId + ")");
        
    // get the product
        IAPProduct product = getProduct(productId);
        if (product == null) {
            purchaseFailed("Unknown product " + productId);
            return;
        }
        
    // purchase
        IABResponseCode code = null;
        try {
            code = iab.purchase(productId,IAB_REQUEST_CODE,"");
        } catch (Exception exception) {
            Log.e(TAG,"Purchase of product " + productId + " failed",exception);
            purchaseFailed("Purchase failed: " + exception.getMessage());
            return;
        }
        
    // if already owned
        if (code == IABResponseCode.ITEM_ALREADY_OWNED) {
            Log.d(TAG,"Product " + productId + " already owned");
            alreadyOwned(productId);
            return;
        }
        
    // error code
        if (code != IABResponseCode.OK) {
            Log.d(TAG,"Purchase of product " + productId + " failed: " + code);
            purchaseFailed("Purchase failed: " + code);
        }
    }
    
    /**
     * Restores purchases (called from the Lua library).
     */
    public static void restorePurchases() {
    	List<IABPurchase> purchases = new ArrayList<IABPurchase>();
    	
   // get purchases
    	IABResponseCode code = null;
    	try {
    		code = iab.getPurchases(purchases);
    	} catch (RemoteException exception) {
    		restorePurchasesFailed(exception.getMessage());
    		return;
    	}
    	if (code != IABResponseCode.OK) {
    	    restorePurchasesFailed(code.toString());
    	    return;
    	}
    	
    // process purchases
    	for (IABPurchase purchase:purchases) {
           alreadyOwned(purchase.getProductId());
    	}
    	purchasesRestored();
    }
    
    /**
     * Called when a product has been purchased.
     */
    private static void purchased(IABPurchase purchase) {
    // get the product
        IAPProduct product = getProduct(purchase.getProductId());
        if (product == null) {
            purchaseFailed("Purchased an unknown product " +
                purchase.getProductId());
            return;
        }        
        
    // consume the purchase if the product is consumable
        if (product.isConsumable()) {
            try {
                iab.consumePurchase(purchase.getPurchaseToken());
            } catch (RemoteException exception) {
                Log.e(TAG,"Error consuming purchase of product " +
                    purchase.getProductId(),exception);
            // Don't return and continue the execution. The user (player)
            // actually purchased the product. It just couldn't be consumed now.
            // It will get consumed during the next call of onResume() on
            // the activity.
            }
        }     
        
        purchased(purchase.getProductId());
    }
    
    /** */
    public static void onCreate() {
        iab.bind();
    }
    
    /** */
    public static void onDestroy() {
        iab.unbind();
    }
    
    /**
     * Handles an activity result.
     * 
     * @param requestCode The request code.
     * @param resultCode The result code.
     * @param data The data.
     * @return <code>true</code> if this is an IAB result,
     *   <code>false</code> otherwise.
     */    
    public static boolean onActivityResult(int requestCode,int resultCode,
        Intent data) {
    //
        if (requestCode == IAB_REQUEST_CODE) {
            iab.onActivityResult(requestCode,resultCode,data);
            return true;
        }    
    
        return false;
    }
}