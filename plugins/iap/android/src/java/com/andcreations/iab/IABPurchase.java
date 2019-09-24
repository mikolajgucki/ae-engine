package com.andcreations.iab;

/**
 * Represents a single purchase.
 * 
 * @author Mikolaj Gucki
 */
public class IABPurchase {
    /** The product identifier. */
    private String productId;
    
    /** The purchase token. */
    private String purchaseToken;
    
    /**
     * Constructs an {@link IABPurchase}.
     *
     * @param productId The product identifier.
     */
    IABPurchase(String productId) {
        this.productId = productId;
    }
    
    /**
     * Gets the product identifier.
     *
     * @return The product identifier.
     */
    public String getProductId() {
        return productId;
    }
    
    /**
     * Sets the purchase token.
     * 
     * @param purchaseToken The purchase token.
     */
    void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }
    
    /**
     * Gets the purchase token.
     * 
     * @return The purchase token.
     */
    public String getPurchaseToken() {
        return purchaseToken;
    }
}