package com.andcreations.iab;

/**
 * @author Mikolaj Gucki
 */
public enum IABResponseCode {
	/** Google Billing response code. See the AIDL file for details. */
	OK(0),
	
	/** Google Billing response code. See the AIDL file for details. */
	USER_CANCELED(1),
	
	/** Google Billing response code. See the AIDL file for details. */
	BILLING_UNAVAILABLE(3),
	
	/** Google Billing response code. See the AIDL file for details. */
	ITEM_UNAVAILABLE(4),
	
	/** Google Billing response code. See the AIDL file for details. */
	DEVELOPER_ERROR(5),
	
	/** Google Billing response code. See the AIDL file for details. */
	ERROR(6),
	
	/** Google Billing response code. See the AIDL file for details. */
	ITEM_ALREADY_OWNED(7),
	
	/** Google Billing response code. See the AIDL file for details. */
	ITEM_NOT_OWNED(8),
	
	/** Indicates that the data signature failed to verify. */
	INVALID_SIGNATURE,

	/** The purchase data cannot be properly parsed. */
    INVALID_PURCHASE_DATA,

    /** The purchase data does not contain the product identifier. */
    NO_PRODUCT_ID,
    
    /** No in-app billing data. */
    NO_IAB_DATA,
    
    /** No purchase data. */
    NO_PURCHASE_DATA,
    
    /** No data signature. */
    NO_DATA_SIGNATURE,
    
    /** No item list. */
    NO_ITEM_LIST,
    
    /** No data list. */   
    NO_DATA_LIST,
    
    /** No signature list. */
    NO_SIGNATURE_LIST,
    
    /** Invalid SKU details data. */
    INVALID_SKU_DETAILS_DATA;
	
    /**
     * Represents no code value.
     */
    private static final int NO_VALUE = -1;
    
	/**
	 * The code value.
	 */
	private int value;
	
	/**
	 * Constructs a {@link IABResponseCode}.
	 */
	private IABResponseCode() {
	    this(NO_VALUE);
	}
	
	/**
	 * Constructs a {@link IABResponseCode}.
	 * 
	 * @param value The value.
	 */
	private IABResponseCode(int value) {
		this.value = value;
	}
	
	/**
	 * Gets the response code from value.
	 * 
	 * @param value The value.
	 * @return The corresponding response code or <tt>null</tt> if there is
	 *   no such response code.
	 */
	public static IABResponseCode fromValue(int value) {
		for (IABResponseCode code:values()) {
			if (code.value == value) {
				return code;
			}
		}
		
		return null;
	}
}
