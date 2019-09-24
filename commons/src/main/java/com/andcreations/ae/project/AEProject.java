package com.andcreations.ae.project;

/**
 * Provides fields and methods related to a AE project.
 *
 * @author Mikolaj Gucki
 */
public class AEProject {
    /** The name of the file with project properties. */
    public static final String PROPERTIES_FILE = "ae.properties";

    /** The path to the build directory. */
    public static final String BUILD_PATH = "build";

    /** The path to the directory with built Lua files. */
    public static final String BUILD_LUA_PATH = "build/lua";
    
    /** The path to the resources relative to the project root. */
    public static final String RESOURCES_PATH = "resources";   
    
    /** The path to the textures relative to the project root. */
    public static final String TEXTURES_PATH = RESOURCES_PATH + "/textures";
    
    /** The path to the fonts relative to the project root. */
    public static final String FONTS_PATH = RESOURCES_PATH + "/fonts";
    
    /** The path to the Android project relative to the project directory. */
    public static final String ANDROID_PROJECT_RELATIVE_PATH = "../android";
    
    /** The path to the iOS project relative to the project directory. */
    public static final String IOS_PROJECT_RELATIVE_PATH = "../ios";
    
    /** The path to the tests. */
    public static final String TESTS_PATH = "tests";
    
    /** The path to the Lua sources of the tests. */
    public static final String TESTS_LUA_PATH = TESTS_PATH + "/lua";
    
    /** The path to the tools. */
    public static final String TOOLS_PATH = "tools";
    
    /** The path to the build directory inside the tools directory. */
    public static final String TOOLS_BUILD_PATH = "tools/build";
    
    /** The path to the libs directory inside the tools directory. */
    public static final String TOOLS_LIBS_PATH = "tools/libs";
    
    /** The path to the Java sources inside the tools directory. */
    public static final String TOOLS_JAVA_SRC = "tools/src/main/java";
    
    /**
     * Provides fields related to assets.
     */
    public static class Assets {
        /** The path to Lua sources in the assets directory. */
        public static final String LUA_PATH = "lua";
        
        /** The path to textures in the assets directory. */
        public static final String TEXTURES_PATH = "textures";
        
        /** The path to sounds in the assets directory. */
        public static final String SOUNDS_PATH = "sounds";
        
        /** The path to music in the assets directory. */
        public static final String MUSIC_PATH = "music";
    }
    
    /** 
     * Environment variables related to a project.
     */
    public static class Env {
        /** The environment variable with a path to a AE distribution. */
        public static final String AE_DIST = "AE_DIST";
    }
    
    /**
     * Provides fields related to the simulator.
     */
    public static class Simulator {
        /** The path to simulator-related files. */
        public static final String PATH = "simulator";
        
        /** The path to the storage directory. */
        public static final String STORAGE_PATH = PATH + "/storage";
        
        /** The path to the file with the simulator state. */
        public static final String STATE_PATH = STORAGE_PATH + "/state";
        
        /** The path to the plugin configuration files. */
        public static final String PLUGINS_PATH = PATH + "/plugins";
    }
}