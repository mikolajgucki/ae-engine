package com.andcreations.iab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import com.android.vending.billing.IInAppBillingService;

import com.andcreations.ae.Log;

/**
 * The in-app billing helper.
 * 
 * @author Mikolaj Gucki
 */
public class IABHelper {
    /** The logcat tag. */
    private static final String TAG = "AE/IAB";
    
    /** The in-app billing version. */
    private static final int VERSION = 3;
    
    /** The in-app billing type. */
    private static final String INAPP_TYPE = "inapp";
    
    /** The key for response code. */
    private static final String KEY_RESPONSE_CODE = "RESPONSE_CODE";
    
    /** The key for the purchase data. */ 
    private static final String KEY_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    
    /** The key for the data signature. */
    private static final String KEY_INAPP_DATA_SIGNATURE =
        "INAPP_DATA_SIGNATURE";
    
    /** The activity. */
    private Activity activity;
    
    /** The application package name. */
    private String packageName;
    
    /** The listener. */
    private IABListener listener;
    
    /** The application public key. */
    private String publicKey;
    
    /** The in-app version. */
    private int version = VERSION;
    
    /** The service interface. */
    private IInAppBillingService service;
    
    /** The service connection. */
    private ServiceConnection connection;
    
    /**
     * Constructs an {@link IABHelper}.
     * 
     * @param activity The application activity.
     * @param listener The in-app billing listener.
     * @param publicKey The application public key.
     */
    public IABHelper(Activity activity,IABListener listener,String publicKey) {
        this.activity = activity;
        this.packageName = activity.getPackageName();
        this.listener = listener;
        this.publicKey = publicKey;
    }
    
    /**
     * Binds the in-app billing service.
     */
    public void bind() {
        Log.d(TAG,"IABHelper.bind()");

    // create service connection
        connection = new ServiceConnection() {
            /** */
            @Override
            public void onServiceDisconnected(ComponentName name) {
                IABHelper.this.service = null;
                Log.d(TAG,"Disconnected from the in-app billing service");
                listener.iabDisconnected();
            }

            /** */
            @Override
            public void onServiceConnected(ComponentName name,IBinder service) {
                IABHelper.this.service =
                    IInAppBillingService.Stub.asInterface(service);
                Log.d(TAG,"Connected to the in-app billing service");
                listener.iabConnected();
            }
        };
        
    // create intent
        Intent intent = new Intent(
            "com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        
    // bind
        boolean bindResult = activity.bindService(
            intent,connection,Context.BIND_AUTO_CREATE);
        if (bindResult == true) {
            Log.d(TAG,"Successfully bound to the in-app billing service");
        } else {
            Log.d(TAG,"Failed to bind to the in-app billing service");
        }
    }
    
    /**
     * Unbinds the in-app billing service.
     */
    public void unbind() {
        Log.d(TAG,"IABHelper.unbind()");
        activity.unbindService(connection);
    }
    
    /**
     * Indicates if the helper is connected to the service.
     *
     * @return <code>true</code> if connected, <code>false</code> otherwise.
     */
    public boolean isConnected() {
        return service != null;
    }
    
    /**
     * Indicates if the billing is supported.
     * 
     * @return The response code.
     * @throws RemoteException on remote error. 
     */
    public IABResponseCode isSupported() throws RemoteException {
        Log.d(TAG,"IABHelper.isSupported()");
        
    // disconnected
        if (service == null) {
            Log.d(TAG,"Disconnected. Cannot check if supported.");
            return null;
        }
        
        return IABResponseCode.fromValue(
            service.isBillingSupported(version,packageName,INAPP_TYPE));
    }
        
    /**
     * Gets SKU details from a JSON object.
     *
     * @param jsonSkuDetails The SKU details.
     * @return The product represented by the JSON.
     * @throws InvalidSKUDetailsException if the details cannot be retrived
     *   from the JSON.
     */
    private IABProduct getSKUDetailsFromJSON(JSONObject jsonSkuDetails)
        throws InvalidSKUDetailsException {
    // identifier
        final String productIdKey = "productId";
        String productId = jsonSkuDetails.optString(productIdKey);
        if (productId == null) {
            throw new InvalidSKUDetailsException(
                "Got product without identifier");
        }
        
    // title
        final String titleKey = "title";
        String title = jsonSkuDetails.optString(titleKey);
        if (title == null) {
            throw new InvalidSKUDetailsException(
                "Got product (" + productId + ") without title");
        }
        
    // price
        final String priceKey = "price";
        String price = jsonSkuDetails.optString(priceKey);
        if (price == null) {
            throw new InvalidSKUDetailsException(
                "Got product (" + productId + ") without price");
        }
        
    // price (cents)
        final String priceMicrosKey = "price_amount_micros";
        int priceInMicros;
        try {
            priceInMicros = jsonSkuDetails.getInt(priceMicrosKey);
        } catch (JSONException exception) {
            throw new InvalidSKUDetailsException(
                "Got product (" + productId + ") without or with invalid" +
                "price in micro-units");
        }
        
    // currency code
        final String currencyCodeKey = "price_currency_code";
        String currencyCode = jsonSkuDetails.optString(currencyCodeKey);
        if (currencyCode== null) {
            throw new InvalidSKUDetailsException(
                "Got product (" + productId + ") without currency code");
        }
        
    // log
        Log.d(TAG,String.format(
            "Got product %s (%s) with price %s (in micros %d)",
            productId,title,price,priceInMicros));
        
        IABProduct product = new IABProduct(
            productId,title,price,priceInMicros,currencyCode);
        return product;
    }    
    
    /**
     * Gets SKU details.
     *
     * @param itemIdList The list of the item (product) identifiers.
     * @param products The result list for the products.
     * @throws RemoteException on remote error.     
     */
    public IABResponseCode getSKUDetails(List<String> itemIdList,
        List<IABProduct> products) throws RemoteException {
    //
        Log.d(TAG,"IABHelper.getSKUDetails()");
            
    // array list
        ArrayList<String> itemIdArrayList = new ArrayList<String>();
        itemIdArrayList.addAll(itemIdList);
        
    // bundle
        final String itemIdListKey = "ITEM_ID_LIST";
        Bundle skusBundle = new Bundle();
        skusBundle.putStringArrayList(itemIdListKey,itemIdArrayList);
        
    // disconnected
        if (service == null) {
            Log.d(TAG,"Disconnected. Cannot get SKU details.");
            return IABResponseCode.OK;
        }        
        
    // call service
        Bundle detailsBundle = service.getSkuDetails(
            version,packageName,INAPP_TYPE,skusBundle);
        
    // response code
        IABResponseCode code = getResponseCodeFromBundle(detailsBundle);
        if (code != IABResponseCode.OK) {
            return code;
        }                
        Log.d(TAG,"SKU details bundle:\n" + toString(detailsBundle));
        
    // get details
        final String detailsListKey = "DETAILS_LIST";
        ArrayList<String> detailsList =
            detailsBundle.getStringArrayList(detailsListKey);
            
    // parse details
        for (String details:detailsList) {
        // parse JSON
            JSONObject jsonSkuDetails = null;
            try {
                jsonSkuDetails = new JSONObject(details);
            } catch (JSONException exception) {
                return IABResponseCode.INVALID_SKU_DETAILS_DATA;
            }
            
        // get product from the JSON
            IABProduct iabProduct;
            try {
                iabProduct = getSKUDetailsFromJSON(jsonSkuDetails);
            } catch (InvalidSKUDetailsException exception) {
                Log.e(TAG,"Error getting SKU from JSON",exception);
                return IABResponseCode.INVALID_SKU_DETAILS_DATA;                
            }
            
            products.add(iabProduct);
        }
        
        return IABResponseCode.OK;
    }
    
    /**
     * Requests to purchase a product. 
     *
     * @param id The product identifier.
     * @param requestCode The request code passed with the activity result.
     * @param payload The developer payload passed with the activity result.
     * @return The response code.
     * @throws RemoteException on remote error.
     * @throws SendIntentException on error sending the intent.
     */
    public IABResponseCode purchase(String id,int requestCode,String payload)
        throws RemoteException,SendIntentException {
    //
        Log.d(TAG,"IABHelper.purchase(" + id + ")");
        
    // disconnected
        if (service == null) {
            Log.d(TAG,"Disconnected. Cannot purchase.");
            return IABResponseCode.OK;
        }      
        
        Bundle buyBundle = service.getBuyIntent(
            version,packageName,id,INAPP_TYPE,payload);
        IABResponseCode code = getResponseCodeFromBundle(buyBundle);
        if (code != IABResponseCode.OK) {
            return code;
        }
        
        final String parcelableKey = "BUY_INTENT";
        PendingIntent pendingIntent = buyBundle.getParcelable(parcelableKey);
        
        activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
            requestCode,new Intent(),Integer.valueOf(0),Integer.valueOf(0),
            Integer.valueOf(0));
    
        return IABResponseCode.OK;
    }
    
    /**
     * Gets the purchase from a JSON object.
     *
     * @param json The JSON object.
     * @return The purchase or <tt>null</tt> if the JSON object does not
     *   contain a product identifier.
     */
    private IABPurchase getPurchaseFromJSON(JSONObject json) {
    // identifier
        final String productIdKey = "productId";
        String productId = json.optString(productIdKey);
        if (productId == null) {
            return null;
        }      
        IABPurchase purchase = new IABPurchase(productId);
        
    // token
        final String purchaseTokenKey = "purchaseToken";
        purchase.setPurchaseToken(json.optString(purchaseTokenKey));
        
        return purchase;
    }
    
    /**
     * Called when a product has been purchased and the data has been verified.
     *
     * @param jsonPurchaseInfo The JSON purchase data.
     */
    private void productPurchased(String jsonPurchaseInfo) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonPurchaseInfo);
        } catch (JSONException exception) {
            listener.iabPurchaseFailed(IABResponseCode.INVALID_PURCHASE_DATA);
            return;
        }
        
    // purchase
        IABPurchase product = getPurchaseFromJSON(json);
        if (product == null) {
            listener.iabPurchaseFailed(IABResponseCode.NO_PRODUCT_ID);
            return;
        }
        
        listener.iabPurchased(product);
    }
    
    /**
     * Gets the user purchases (700 at most as the continuation token is not
     * considered).
     *
     * @param purchasedProducts The result list for the purchased products.
     * @throws RemoteException on remote error. 
     */
    public IABResponseCode getPurchases(List<IABPurchase> purchasedProducts)
        throws RemoteException {
    //
        Log.d(TAG,"IABHelper.getPurchases()");
        
    // disconnected
        if (service == null) {
            Log.d(TAG,"Disconnected. Cannot get purchases.");
            return IABResponseCode.OK;
        }  
        
        Bundle products = service.getPurchases(
            version,packageName,INAPP_TYPE,null);
        
        IABResponseCode code = getResponseCodeFromBundle(products);
        if (code == IABResponseCode.OK) {
            Log.d(TAG,"Purchased products bundle:\n" + toString(products));
            
        // item list
            final String itemListKey = "INAPP_PURCHASE_ITEM_LIST";
            ArrayList<String> itemList = products.getStringArrayList(
                itemListKey);
            if (itemList == null) {
                return IABResponseCode.NO_ITEM_LIST;
            }
            
        // data list
            final String dataListKey = "INAPP_PURCHASE_DATA_LIST";
            ArrayList<String> dataList = products.getStringArrayList(
                dataListKey);
            if (dataList == null) {
                return IABResponseCode.NO_DATA_LIST;
            }
            
        // signature list
            final String signatureListKey = "INAPP_DATA_SIGNATURE_LIST";
            ArrayList<String> signatureList = products.getStringArrayList(
                signatureListKey);
            if (signatureList == null) {
                return IABResponseCode.NO_SIGNATURE_LIST;
            }
            
        // for each item
            for (int index = 0; index < dataList.size(); index++) {
                String purchaseData = dataList.get(index);
                String dataSignature = signatureList.get(index);
                                
            // verify signature
                try {
                    if (IABSecurity.verifySignature(publicKey,dataSignature,
                        purchaseData) == false) {
                    //
                        return IABResponseCode.INVALID_SIGNATURE;
                    }
                } catch (SignatureVerificationException exception) {
                    Log.e(TAG,"Signature verification error",exception);
                    return IABResponseCode.INVALID_SIGNATURE;
                }
                
            // parse the purchase data
                JSONObject json = null;
                try {
                    json = new JSONObject(purchaseData);
                } catch (JSONException exception) {
                    return IABResponseCode.INVALID_PURCHASE_DATA;
                }

            // get the purchase
                IABPurchase purchase = getPurchaseFromJSON(json);
                if (purchase == null) {
                    return IABResponseCode.NO_PRODUCT_ID;
                }                
                
                purchasedProducts.add(purchase);
            }
        }
        
        return code;
    }
    
    /**
     * Consumes a purchase.
     * 
     * @param purchaseToken The purchase token.
     * @throws RemoteException on remote error.
     */
    public IABResponseCode consumePurchase(String purchaseToken)
        throws RemoteException {
    //
        Log.d(TAG,"IABHElper.consumePurchase()");
        
    // disconnected
        if (service == null) {
            Log.d(TAG,"Disconnected. Cannot consume purchases.");
            return IABResponseCode.OK;
        }  
        
        int code = service.consumePurchase(version,packageName,purchaseToken);
        return IABResponseCode.fromValue(code);
    }
    
    /**
     * Handles the activity result.
     * 
     * @param requestCode The request code.
     * @param resultCode The result code.
     * @param data The data.
     */
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        Log.d(TAG,"IABHelper.onActivityResult()");
        
        if (data == null) {
            listener.iabPurchaseFailed(IABResponseCode.NO_IAB_DATA);
            return;
        }
        
        String purchaseData = data.getStringExtra(KEY_INAPP_PURCHASE_DATA);
        if (purchaseData == null) {
            listener.iabPurchaseFailed(IABResponseCode.NO_PURCHASE_DATA);
            return;
        }
        
        String dataSignature = data.getStringExtra(KEY_INAPP_DATA_SIGNATURE);
        if (dataSignature == null) {
            listener.iabPurchaseFailed(IABResponseCode.NO_DATA_SIGNATURE);
            return;
        }
        
        IABResponseCode responseCode = getResponseCodeFromIntent(data);
        if (resultCode == Activity.RESULT_OK) {
            if (responseCode == IABResponseCode.OK) {
                try {
                    if (IABSecurity.verifySignature(publicKey,dataSignature,
                        purchaseData) == false) {
                    //
                        listener.iabPurchaseFailed(
                            IABResponseCode.INVALID_SIGNATURE);
                        return;
                    }                    
                } catch (SignatureVerificationException exception) {
                    listener.iabPurchaseFailed(exception.getMessage());
                }
                
                productPurchased(purchaseData);
            }
            else if (responseCode == IABResponseCode.USER_CANCELED) {
                listener.iabPurchaseCanceled();
            }
            else {
                listener.iabPurchaseFailed(responseCode);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            listener.iabPurchaseCanceled();
        } else {
            listener.iabPurchaseFailed("Unknown purchase response");
        }
    }    
    
    /**
     * Gets the response code from a bundle.
     * 
     * @param bundle The bundle.
     * @return The response code.
     */
    private static IABResponseCode getResponseCodeFromBundle(Bundle bundle) {
        Object obj = bundle.get(KEY_RESPONSE_CODE);
        if (obj == null) {
            return IABResponseCode.OK;            
        }
        
        if (obj instanceof Integer) {
            return IABResponseCode.fromValue(((Integer)obj).intValue());
        }
        if (obj instanceof Long) {
            return IABResponseCode.fromValue(((Long)obj).intValue());
        }
        
        throw new RuntimeException("Unexpected type for bundle response " +
            "code:" + obj.getClass().getName());
    }
    
    /**
     * Gets the response code from an intent.
     *
     * @param intent The intent.
     * @return The response code.
     */
    private static IABResponseCode getResponseCodeFromIntent(Intent intent) {
        Object obj = intent.getExtras().get(KEY_RESPONSE_CODE);
        if (obj == null) {
            return IABResponseCode.OK;
        }            
        
        if (obj instanceof Integer) {
            return IABResponseCode.fromValue(((Integer)obj).intValue());
        }
        if (obj instanceof Long) {
            return IABResponseCode.fromValue(((Long)obj).intValue());
        }
        
        throw new RuntimeException("Unexpected type for intent response " +
            "code:" + obj.getClass().getName());        
    }
    
    /**
     * Converts a bundle into human readable string.
     * 
     * @param bundle The bundle.
     * @return The string.
     */
    private static String toString(Bundle bundle) {
        StringBuilder builder = new StringBuilder();
        
        Set<String> keys = bundle.keySet();
        for (String key:keys) {
            builder.append(key + " = " + bundle.get(key) + "\n");
        }
        
        return builder.toString();
    }
}
