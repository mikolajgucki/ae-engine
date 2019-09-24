package com.andcreations.iab;

/**
 * @author Mikolaj Gucki
 */
public interface IABListener {
    /**
     * Called when fails to bind to the service.
     */
    void iabFailedToBind();
    
	/**
	 * Called when the in-app billing has been connected.
	 */
	void iabConnected();
	
	/**
	 * Called when the in-app billing has been disconnected.
	 */
	void iabDisconnected();
	
	/**
	 * Called when a purchase failed.
	 *
	 * @param message The failure reason.
	 */
	void iabPurchaseFailed(String message);
	
	/**
	 * Called when a purchase failed.
	 *
	 * @param responseCode The railure reason.
	 */
	void iabPurchaseFailed(IABResponseCode responseCode);
	
	/**
	 * Called when a purchase has been canceled.
	 */
	void iabPurchaseCanceled();
	
	/**
	 * Called when a product has been purchased.
	 *
	 * @param purchase The purchase.
	 */
	void iabPurchased(IABPurchase purchase);
}
