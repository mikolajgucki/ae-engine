package com.andcreations.iab;

/**
 * Represents a Google in-app billing product.
 *
 * @author Mikolaj Gucki
 */
public class IABProduct {
    /** The product identifier. */
    private String id;
    
    /** The title. */
    private String title;
    
    /** The price. */
    private String price;
    
    /** The price in micro-units. */
    private int priceInMicros;
    
    /** The currency code (ISO 4217). */
    private String currencyCode;
    
    /**
     * Constructs an {@link IABProduct}.
     *
     * @param id The product identifier.
     * @param title The title.
     * @param price The price.
     * @param priceInMicros The price in micro-units.
     * @param currencyCode The currency code (ISO 4217).
     */
    public IABProduct(String id,String title,String price,int priceInMicros,
        String currencyCode) {
    //
        this.id = id;
        this.title = title;
        this.price = price;
        this.priceInMicros = priceInMicros;
        this.currencyCode = currencyCode;
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
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the price.
     *
     * @return The price.
     */
    public String getPrice() {
        return price;
    }    
    
    /**
     * Gets the price in micro-units.
     *
     * @return The price in micro-units.
     */
    public int getPriceInMicros() {
        return priceInMicros;
    }
    
    /**
     * Gets the currency code (ISO 4217).
     *
     * @return The currency code.
     */
    public String getCurrencyCode() {
        return currencyCode;
    }
}