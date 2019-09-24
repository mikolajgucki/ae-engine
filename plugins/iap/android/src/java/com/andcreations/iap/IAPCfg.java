package com.andcreations.iap;

import java.util.List;

/**
 * The IAP configuration.
 *
 * @author Mikolaj Gucki
 */
public class IAPCfg {
    /** The products that can be purchased. */
    private List<IAPProduct> products;
    
    /** Indicates if to retrieve product details. */
    private boolean retrieveProductDetails;
    
    /**
     * Constructs an {@link IAPCfg}.
     *
     * @param products The products that can be purchased.
     * @param retrieveProductDetails Indicates if to retrieve products
     *   information.
     */
    public IAPCfg(List<IAPProduct> products,boolean retrieveProductDetails) {
        this.products = products;
        this.retrieveProductDetails = retrieveProductDetails;
    }
    
    /**
   * Constructs an {@link IAPCfg}. Does not retrive the products information.
     *
     * @param products The products that can be purchased.
     */
    public IAPCfg(List<IAPProduct> products) {
        this(products,false);
    }
    
    /**
     * Gets the products.
     *
     * @return The products.
     */
    public List<IAPProduct> getProducts() {
        return products;
    }
    
    /**
     * Checks if to retrive product details.
     *
     * @return <code>true</code> if to retrieve, <code>false</code> otherwise.
     */
    public boolean getRetrieveProductDetails() {
        return retrieveProductDetails;
    }
}