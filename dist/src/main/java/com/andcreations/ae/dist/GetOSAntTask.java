package com.andcreations.ae.dist;

import com.andcreations.ant.AETask;
import com.andcreations.ant.AntSingleValue;
import com.andcreations.system.OS;

/**
 * Determines the operating system (windows, unix, mac, unknown) and stores in
 * property <code>ae.os</code>.
 *
 * @author Mikolaj Gucki
 */
public class GetOSAntTask extends AETask {
    /** The name of the destination property. */
    private AntSingleValue dstProp;    
    
    /** */
    public AntSingleValue createDstProp() {
        if (dstProp != null) {
            duplicatedElement("dstprop");
        }
        
        dstProp = new AntSingleValue();
        return dstProp;
    }
    
    /** */
    @Override
    public void execute() {
        verifyElementSet(dstProp,"dstprop");
        String propValue = OS.getOS();                
        getProject().setProperty(dstProp.getValue(),propValue);
        getProject().setProperty(dstProp.getValue() + "." + propValue,"true");
    }
}