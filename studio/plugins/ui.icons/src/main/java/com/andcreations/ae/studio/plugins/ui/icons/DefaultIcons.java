package com.andcreations.ae.studio.plugins.ui.icons;

/**
 * Contains the names of the default icons.
 * 
 * @author Mikolaj Gucki
 */
public class DefaultIcons {
    public static final String EMPTY = "empty";
    public static final String FILE = "file";
    public static final String PROJECT_FILE = "project_file";
    public static final String DIRECTORY = "directory";
    public static final String PROJECT_DIRECTORY = "project_directory";
    public static final String PROBLEM = "problem";
    public static final String INFO = "info";
    public static final String ERROR = "error";
    public static final String ERROR_SMALL = "error_small";
    public static final String WARNING = "warning";
    public static final String WARNING_SMALL = "warning_small";
    public static final String OK = "ok";
    public static final String QUESTION = "question";
    public static final String DELETE = "delete";
    public static final String DELETE_ALL = "delete_all";
    public static final String CLOSE_VIEW = "close_view";
    public static final String MAXIMIZE_VIEW = "maximize_view";
    public static final String LOCK = "lock";
    public static final String ERASER = "eraser";
    public static final String VALIDATION_ERROR = "validation_error";
    public static final String SEARCH = "search";
    public static final String REFRESH = "refresh";
    public static final String DEBUG = "debug";
    public static final String DROPDOWN = "dropdown";
    public static final String VIEWS = "views";
    public static final String CLOSE_VIEWS = "close_views";
    public static final String UPWARDS = "upwards";
    public static final String DOWNWARDS = "downwards";
    public static final String SYNC = "sync";
    public static final String SETTINGS = "settings";
    public static final String IMAGE = "image";
    public static final String FIELD = "field";
    public static final String POINTER = "pointer";
    public static final String PIN = "pin";
    public static final String PLAY = "play";
    public static final String RESTART = "restart";
    public static final String PAUSE = "pause";
    public static final String RESUME = "resume";
    public static final String STOP = "stop";
    public static final String EXPAND = "expand";
    public static final String COLLAPSE = "collapse";
    public static final String TERMINATE = "terminate";
    public static final String AZ = "az";
    public static final String TEST = "test";
    public static final String TEST_RUNNING = "test_running";
    public static final String TEST_OK = "test_ok";
    public static final String TEST_FAILED = "test_failed";
    public static final String TESTS = "tests";
    public static final String TESTS_RUNNING = "tests_running";
    public static final String TESTS_OK = "tests_ok";
    public static final String TESTS_FAILED = "tests_failed";
    public static final String KILL = "kill";
    public static final String KILL_HIGHLIGHT = "kill_highlight";
    public static final String SAVE = "save";
    public static final String AE_STUDIO = "ae_studio";
    
    public static final String DECO_ADD = "deco_add";
    public static final String DECO_ERROR = "deco_error";
    public static final String DECO_WARNING = "deco_warning";
    public static final String DECO_MODIFIED = "deco_modified";
    public static final String DECO_ERASER = "deco_eraser";
    public static final String DECO_POINTER = "deco_pointer";
    public static final String DECO_PLAY = "deco_play";
    public static final String DECO_TEST = "deco_test";
    
    /** */
    static void init() {
    // source
        Icons.addIconSource(
            new ResourceIconSource(DefaultIcons.class,"resources/"));
        
    // colorize
        Icons.colorize(FILE,Icons.gray());
        Icons.colorize(PROJECT_FILE,Icons.lightGray());
        Icons.colorize(DIRECTORY,Icons.darkGreen());
        Icons.colorize(PROJECT_DIRECTORY,Icons.green());
        Icons.colorize(PROBLEM,Icons.orange());
        Icons.colorize(INFO,Icons.blue());
        Icons.colorize(ERROR,Icons.red());
        Icons.colorize(ERROR_SMALL,Icons.red());
        Icons.colorize(WARNING,Icons.orange());
        Icons.colorize(WARNING_SMALL,Icons.orange());
        Icons.colorize(OK,Icons.green());
        Icons.colorize(QUESTION,Icons.blue());
        Icons.colorize(CLOSE_VIEW,Icons.red());
        Icons.colorize(MAXIMIZE_VIEW,Icons.green());
        Icons.colorize(LOCK,Icons.orange());
        Icons.colorize(ERASER,Icons.orange());
        Icons.colorize(VALIDATION_ERROR,Icons.red());
        Icons.colorize(SEARCH,Icons.blue());
        Icons.colorize(REFRESH,Icons.green());
        Icons.colorize(DELETE,Icons.red());
        Icons.colorize(DELETE_ALL,Icons.red());
        Icons.colorize(DEBUG,Icons.blue());
        Icons.colorize(DROPDOWN,Icons.gray());
        Icons.colorize(VIEWS,Icons.gray());
        Icons.colorize(CLOSE_VIEWS,Icons.gray());
        Icons.colorize(UPWARDS,Icons.green());
        Icons.colorize(DOWNWARDS,Icons.green());
        Icons.colorize(SYNC,Icons.gray());
        Icons.colorize(SETTINGS,Icons.green());
        Icons.colorize(IMAGE,Icons.gray());
        Icons.colorize(FIELD,Icons.gray());
        Icons.colorize(POINTER,Icons.gray());
        Icons.colorize(PIN,Icons.darkGray());
        Icons.colorize(PLAY,Icons.green());
        Icons.colorize(RESTART,Icons.green());
        Icons.colorize(PAUSE,Icons.orange());
        Icons.colorize(RESUME,Icons.orange());
        Icons.colorize(STOP,Icons.red());
        Icons.colorize(EXPAND,Icons.green());
        Icons.colorize(COLLAPSE,Icons.orange());
        Icons.colorize(TERMINATE,Icons.red());
        Icons.colorize(AZ,Icons.gray());
        Icons.colorize(TEST,Icons.orange());
        Icons.colorize(TEST_RUNNING,Icons.gray());
        Icons.colorize(TEST_OK,Icons.green());
        Icons.colorize(TEST_FAILED,Icons.red());
        Icons.colorize(TESTS,Icons.orange());
        Icons.colorize(TESTS_RUNNING,Icons.gray());
        Icons.colorize(TESTS_OK,Icons.green());
        Icons.colorize(TESTS_FAILED,Icons.red());
        Icons.colorize(KILL,Icons.red());
        Icons.colorize(SAVE,Icons.blue());
        Icons.colorize(KILL_HIGHLIGHT,Icons.vividRed());        
        
        Icons.colorize(DECO_ADD,Icons.orange());
        Icons.colorize(DECO_ERROR,Icons.red());
        Icons.colorize(DECO_WARNING,Icons.orange());
        Icons.colorize(DECO_MODIFIED,Icons.orange());
        Icons.colorize(DECO_ERASER,Icons.orange());
        Icons.colorize(DECO_POINTER,Icons.gray());
        Icons.colorize(DECO_PLAY,Icons.green());
        Icons.colorize(DECO_TEST,Icons.orange());
    }
}
