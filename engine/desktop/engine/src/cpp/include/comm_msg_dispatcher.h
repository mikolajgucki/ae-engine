#ifndef AE_COMM_MSG_DISPATCHER_H
#define AE_COMM_MSG_DISPATCHER_H

#include "InputStream.h"
#include "CommDebugger.h"
#include "CommDesktopEngineListener.h"

namespace ae {
    
namespace engine {
    
namespace desktop {

/**
 * \brief Initializes the dispatcher.
 * \param input_ The input stream from which to read the messages.
 * \param debugger_ The debugger.
 * \param listener_ The engine listener.
 */
void initCommMsgDispatcher(ae::io::InputStream *input_,CommDebugger *debugger_,
    CommDesktopEngineListener *listener_);
    
/**
 * \brief Runs the dispatcher.
 */
void runCommMsgDispatcher();

/**
 * \brief Stops the dispatcher.
 */
void stopCommMsgDispatcher();
    
} // namespace
    
} // namespace
    
} // namespace

#endif // AE_COMM_MSG_DISPATCHER_H