#ifndef AE_DESKTOP_ENGINE_H
#define AE_DESKTOP_ENGINE_H

#include "Logger.h"
#include "DesktopEngineCfg.h"
#include "DesktopEngineListener.h"

namespace ae {

namespace engine {

namespace desktop {

/**
 * \brief Runs the desktop engine with the configuration specified.
 * \param The logger.
 * \param cfg_ The configuration.
 * \param listener_ The listener.
 */
void runDesktopEngine(Logger *logger_,DesktopEngineCfg *cfg_,
    DesktopEngineListener *listener_ = (DesktopEngineListener *)0);

/**
 * \brief Pauses the engine (asynchronously).
 */
void pauseDesktopEngine();

/**
 * \brief Resumes the engine (asynchronously).
 */
void resumeDesktopEngine();

/**
 * \brief Restarts the engine (asynchronously).
 */
void restartDesktopEngine();

/**
 * \brief Stops the engine (asynchronously).
 */
void stopDesktopEngine();

/**
 * \brief Sets stats logging enabled/disabled.
 */
void setLogStatsEnabled(bool enabled);

/**
 * \brief Sets the volume factor.
 * \param volumeFactor_ The volume factor (0..1).
 * \param notify Indicates if to notify the listener.
 * \return <code>true</code> on success, <code>false</code> otherwise.
 */
bool setVolumeFactor(double volumeFactor_,bool notify = true);

} // namespace

} // namespace

} // namespace

#endif // AE_DESKTOP_ENGINE_H