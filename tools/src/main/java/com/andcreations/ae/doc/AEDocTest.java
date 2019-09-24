package com.andcreations.ae.doc;

import java.io.File;

/**
 * @author Mikolaj Gucki
 */
public class AEDocTest {
    /** */
    public static void main(String[] args) throws Exception {
        AEDocCfg cfg = new AEDocCfg(
            new File[]{new File("doc/in")},new File("doc/out"));
        AEDoc doc = new AEDoc(cfg);
        doc.process();
    }
}
