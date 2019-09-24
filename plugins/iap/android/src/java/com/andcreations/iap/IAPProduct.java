package com.andcreations.iap;

import com.andcreations.iab.IABProduct;

/**
 * Represents a product.
 *
 * @author Mikolaj Gucki
 */
public class IAPProduct {
    /**
     * Product type.
     */
    public static enum Type {
        /** Consumable product. */
        CONSUMABLE,
        
        /** Non-consumable product. */
        NON_CONSUMABLE
    }
    
    /** The product identifier. */
    private String id;
    
    /** The product type. */
    private Type type;
    
    /** The related IAB product. */
    private IABProduct iabProduct;
    
    /**
     * Constructs an {@link IAPProduct}.
     *
     * @param id The product identifier.
     * @param type The product type.
     */
    public IAPProduct(String id,Type type) {
        this.id = id;
        this.type = type;
    }
    
    /**
     * Gets the product identifier.
     *
     * @return The product identifier.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Checks if the product is consumable.
     *
     * @return <code>true</code> if consumable, <code>false</code> otherwise.
     */
    public boolean isConsumable() {
        return type == Type.CONSUMABLE;
    }
    
    /**
     * Sets the related IAB product.
     *
     * @param iabProduct The IAB product.
     */
    void setIABProduct(IABProduct iabProduct) {
        this.iabProduct = iabProduct;
    }
    
    /**
     * Gets the related IAB product.
     *
     * @return The IAB product.
     */
    public IABProduct getIABProduct() {
        return iabProduct;
    }
}