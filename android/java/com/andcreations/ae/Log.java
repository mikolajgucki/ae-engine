package com.andcreations.ae;

/**
 * @author Mikolaj Gucki
 */
public class Log {
    /** */
    public static void d(String tag,String msg) {
        if (AE.isDebug() == false) {
            return;
        }
        android.util.Log.d(tag,msg);
    }
    
    /** */
    public static void d(String tag,String msg,Throwable tr) {
        if (AE.isDebug() == false) {
            return;
        }
        android.util.Log.d(tag,msg,tr);
    }
    
    /** */
    public static void w(String tag,String msg) {
        if (AE.isDebug() == false) {
            return;
        }
        android.util.Log.w(tag,msg);
    }
    
    /** */
    public static void e(String tag,String msg) {
        if (AE.isDebug() == false) {
            return;
        }
        android.util.Log.e(tag,msg);
    }
    
    /** */
    public static void e(String tag,String msg,Throwable tr) {
        if (AE.isDebug() == false) {
            return;
        }
        android.util.Log.e(tag,msg,tr);
    }    
}