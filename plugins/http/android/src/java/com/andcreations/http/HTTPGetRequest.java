package com.andcreations.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mikolaj Gucki
 */
class HTTPGETRequest implements Runnable {
    /** */
    private String id;
    
    /** */
    private String urlString;
    
    /** */
    HTTPGETRequest(String id,String urlString) {
        this.id = id;
        this.urlString = urlString;
    }
    
    /** */
    private void handleException(Exception exception) {
        AEHTTP.failedToReceiveResponse(id,exception.getMessage());
    }
    
    /** */
    @Override
    public void run() {
    // URL
        URL url;
        try {
            url = new URL(urlString);
        } catch(MalformedURLException exception) {
            handleException(exception);
            return;
        }
        
    // open connection and get input stream
        HttpURLConnection connection;
        InputStream input;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(AEHTTP.CONNECT_TIMEOUT);
            connection.setReadTimeout(AEHTTP.READ_TIMEOUT);
            connection.connect();
            input = connection.getInputStream();
        } catch(IOException exception) {
            handleException(exception);
            return;
        }
        
        String responseBody;
        int responseCode;
        try {
            responseBody = read(input);
            responseCode = connection.getResponseCode();
        } catch(IOException exception) {
            handleException(exception);
            return;
        }
        
        AEHTTP.receivedResponse(id,responseCode,responseBody);
    }
    
    /** */
    public String read(InputStream stream) throws IOException,
        UnsupportedEncodingException {
    //
        Reader reader = new InputStreamReader(stream,"UTF-8");        
        StringBuilder builder = new StringBuilder();
        
        // TODO Speed up by reading buffers instead of byte by byte.
        while (true)
        {
            int value = reader.read();
            if (value < 0)
            {
                break;
            }
            builder.append((char)value);
        }
        
        return builder.toString();
    }
}
