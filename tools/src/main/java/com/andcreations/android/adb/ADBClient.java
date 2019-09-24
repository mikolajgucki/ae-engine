package com.andcreations.android.adb;

import com.android.ddmlib.Client;

/**
 * @author Mikolaj Gucki
 */
public class ADBClient {
    /** */
    private Client client;
    
    /** */
    ADBClient(Client client) {
        this.client = client;
    }
    
    /** */
    public int getPid() {
        return client.getClientData().getPid();
    }
}