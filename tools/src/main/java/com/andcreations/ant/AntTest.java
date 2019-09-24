package com.andcreations.ant;

import java.io.File;
import java.util.Map;

/** */
public class AntTest {
    public static void main(String[] args) throws Exception {
        AntTargets t = new AntTargets();
        Map<String,String> targets = t.listTargets(new File("."));
        for (String name:targets.keySet()) {
            System.out.println(name + ": " + targets.get(name));
        }
    }
}