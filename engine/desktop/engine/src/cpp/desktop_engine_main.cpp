#include <memory>
#include <cstdio>
#include <cstring>

#include "SDL_thread.h"
#include "Log.h"
#include "FileLogger.h"
#include "DebugLog.h"
#include "StandardOutputStream.h"
#include "StandardInputStream.h"
#include "Base16OutputStream.h"
#include "Base16InputStream.h"
#include "ConsoleLogger.h"
#include "CommConsole.h"
#include "CommLogger.h"
#include "CommMapWriter.h"
#include "CommDesktopEngineListener.h"
#include "PropertyParser.h"
#include "cfg_reader.h"
#include "desktop_engine.h"
#include "comm_msg_dispatcher.h"

#include "FileInputStream.h"
#include "BufferedInputStream.h"
#include "UnhashInputStream.h"

using namespace std;
using namespace ae;
using namespace ae::io;
using namespace ae::engine::desktop;

/// The log tag.
static const char *const logTag = "desktop.engine";

/// The log tag for the debug log.
static const char *const debugLogTag = "main";

/// The debug log argument.
static const char *const debugLogArg = "--debug-log=";

/// The logger.
static Logger *logger = (Logger *)0;

/// The debug log logger.
static FileLogger *debugLogLogger = (FileLogger *)0;

/// The configuration.
static DesktopEngineCfg *cfg = (DesktopEngineCfg *)0;

/// Indicates if to run the desktop engine with communication over the
/// standard input/output.
static bool comm = false;

/// Indicates if to enable the debugger over the standard input/output.
static bool commDebuggerEnabled = false;

/// The message pack map writer for the communication.
static CommMapWriter *commMapWriter = (CommMapWriter *)0;

/// The engine listener.
static CommDesktopEngineListener *listener = (CommDesktopEngineListener *)0;

/// The message dispatcher thread.
static SDL_Thread *commMsgDispatcherThread = (SDL_Thread *)0;

/// The input stream for the communication.
static InputStream *inputStream = (InputStream *)0;

/// The output stream for the communication.
static OutputStream *outputStream = (OutputStream *)0;

/// The debugger for the communication.
static CommDebugger *commDebugger = (CommDebugger *)0;

// To be able write custom main()
#undef main

/** */
static void logSection(const string &name) {
    ostringstream msg;
    msg << "-----------8<------------ " << name;
    Log::info(logTag,msg.str());
}

/** */
static void setupDebugLog(const char *arg) {
    string argName;
    string argValue;
    
    PropertyParser parser;
    if (parser.parse(string(arg),argName,argValue) == false) {
        cerr << "Failed to parse debug log option: " << parser.getError();
        exit(-1);
    }
    
    debugLogLogger = new (nothrow) FileLogger(argValue.c_str());
    if (debugLogLogger == (FileLogger *)0) {
        cerr << "No memory";
        exit(-1);
    }
    if (debugLogLogger->init() == false) {
        cerr << "Failed to initialize debug log: " << debugLogLogger->getError();
        exit(-1);
    }
    
    DebugLog::init(debugLogLogger);
}

/** */
static void parseArgs(int argc,char **argv) {
// for each argument
    int index = 1;
    while (index < argc) {
        char *arg = argv[index];
        index++;
        
        if (strlen(arg) == 0 || arg[0] != '-') {
            continue;
        }
        
        if (strcmp(arg,"--comm") == 0) {
            comm = true;
            continue;
        }
        
        if (strcmp(arg,"--no-stats") == 0) {
            setLogStatsEnabled(false);
            continue;
        }
        
        if (strcmp(arg,"--comm-debug") == 0) {
            commDebuggerEnabled = true;
            continue;
        }
        
        if (strncmp(arg,debugLogArg,strlen(debugLogArg)) == 0) {
            setupDebugLog(arg);
            continue;
        }
        
        cerr << "Unknown option " << arg;
        exit(-1);
    }
}

/** */
static map<string,string> getArgProps(int argc,char **argv) {
    PropertyParser parser;
    map<string,string> props;
    
// for each argument
    for (int index = 1; index < argc; index++) {
        char *arg = argv[index];
        if (strlen(arg) == 0 || arg[0] == '-') {
            continue;
        }
        
        string key;
        string value;
    // parse
        if (parser.parse(string(argv[index]),key,value) == false) {
            ostringstream err;
            err << "Failed to parse argument " << arg << " as property: " <<
                parser.getError();
            Log::error(logTag,err.str());
            exit(-1);
        }
        
        props[key] = value;
    }
    
    return props;
}

/** */
static int commMsgDispatcherThreadFunc(void *ptr) {
    runCommMsgDispatcher();
    return 0;
}

/** 
 * \brief Initializes the desktop engine.
 */
static void initialize(int argc,char **argv) {
// disable standard input/output/error buffering
    setbuf(stdin,(char *)0);
    setbuf(stdout,(char *)0);
    setbuf(stderr,(char *)0);
    
// AE distribution directory
    const char *aeDistVar = "AE_DIST";
    const char *aeDistDir = getenv(aeDistVar);
    if (aeDistDir == (const char *)0) {
        ostringstream err;
        err << "Environment variable " << aeDistVar << " not set";
        Log::error(logTag,err.str());
        exit(-1);        
    }
    
// arguments
    parseArgs(argc,argv);
    
// log AE distribution directory
    ostringstream msg;
    msg << "AE distribution directory is " << aeDistDir;
    DebugLog::trace(debugLogTag,msg.str());
    
// communication
    if (comm == true) {
        DebugLog::trace(debugLogTag,
            "Starting communication (--comm) over standard input/output");
        
    // streams
        inputStream = new Base16InputStream(
            StandardInputStream::getInstance());
        outputStream = new Base16OutputStream(
            StandardOutputStream::getInstance());
        
    // map writer
        commMapWriter = new CommMapWriter(outputStream);
    }
    
// logger
    if (comm == false) {
        DebugLog::trace(debugLogTag,"Creating console logger");
        logger = new ConsoleLogger();
    }
    else {
        DebugLog::trace(debugLogTag,"Creating comm (-comm) logger");
        logger = new CommLogger(outputStream);
    }
    Log::init(logger);
    logSection("start");
    
// configuration
    map<string,string> argProps = getArgProps(argc,argv);
    cfg = readCfg(argProps);
    
// message dispatcher
    if (comm == true) {
    // debugger
        if (commDebuggerEnabled == true) {
            DebugLog::trace(debugLogTag,"Starting comm (--comm) debugger");
            commDebugger = new CommDebugger(commMapWriter);
        }
        
    // engine listener
        listener = new CommDesktopEngineListener(commMapWriter,commDebugger);
        if (listener->chkError() == true) {
            Log::error(logTag,listener->getError());
            exit(-1);            
        }
        
    // initialize the message dispatcher
        initCommMsgDispatcher(inputStream,commDebugger,listener);
        
    // thread
        commMsgDispatcherThread = SDL_CreateThread(
            commMsgDispatcherThreadFunc,"CommMsgDispatcher",(void *)0);
        if (commMsgDispatcherThread == (SDL_Thread *)0) {
            Log::error(logTag,"Failed to create message dispatcher thread");
            exit(-1);
        }
    }
}

/** */
static void finalize() {
    if (listener != (CommDesktopEngineListener *)0) {
        delete listener;
    }
    if (commMapWriter != (CommMapWriter *)0) {
        delete commMapWriter;
    }
    if (outputStream != (OutputStream *)0) {
        delete outputStream;
    }
    if (inputStream != (InputStream *)0) {
        delete inputStream;
    }
    
    delete cfg;
    delete logger;
    
    if (debugLogLogger != (FileLogger *)0) {
        delete debugLogLogger;
    }
}

/** */
int launchDesktopEngine(int argc,char **argv) {
    initialize(argc,argv);
    runDesktopEngine(logger,cfg,listener);
    finalize();
    
    return 0;
}
