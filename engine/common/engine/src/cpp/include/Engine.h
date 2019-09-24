#ifndef AE_ENGINE_H
#define AE_ENGINE_H

#include <vector>

#include "Error.h"
#include "Model.h"
#include "EngineMutex.h"
#include "EngineMutexFactory.h"
#include "EngineRequest.h"
#include "EngineCfg.h"

namespace ae {
    
namespace engine {
  
/**
 * \brief The engine class.
 */
class Engine:public Error {
    /// The engine configuration.
    EngineCfg cfg;
    
    /// The display width.
    int width;
    
    /// The display height.
    int height;
    
    /// The models contained in this engine.
    std::vector<Model *> models;    
    
    /// The engine requests run between the frames.
    std::vector<EngineRequest *> requests;
    
    /// The mutex which locks the requests.
    EngineMutex *requestsMutex;
    
    /**
     * \brief Creates the engine.
     */
    void create();
    
public:
    /**
     * \brief Constructs an Engine.
     * \param cfg_ The engine configuration.
     */
    Engine(EngineCfg cfg_);
    
    /**
     * \brief Gets the engine configuration.
     * \return The configuration.
     */
    EngineCfg &getCfg() {
        return cfg;
    }
    
    /**
     * \brief Gets the display width.
     * \return The display width.
     */ 
    int getWidth() const {
        return width;
    }
    
    /**
     * \brief Gets the display height.
     * \return The display height.
     */ 
    int getHeight() const {
        return height;
    }
    
    /**
     * \brief Starts the engine.
     */
    void start();
    
    /**
     * \brief Stops the engine.
     */
    void stop();
    
    /**
     * \brief Invoked when the display has been resized.
     * \param _width The display width.
     * \param _height The display height.
     */
    void displayResized(int _width,int _height);
    
    /**
     * \brief Adds a model at the beginning of the model list.
     * \param model The model;
     */
    void prependModel(Model *model);
    
    /** 
     * \brief Adds a model at the of the model list.
     * \param model The model.
     */
    void appendModel(Model *model);
    
    /**
     * \brief Removes a model.
     * \param model The model to remove.
     */
    void removeModel(Model *model);
    
    /**
     * \brief Draws all the models.
     * \return <code>true</code> if drawing all the models has been successful,
     *     sets error and returns <code>false</code> otherwise.
     */
    bool modelDraw();
    
    /**
     * \brief Updates all the model.
     * \param time The time elapsed since the last from in milliseconds.
     * \param anyUpdated Sets <code>true</code> if at least one model has
     *     changed, <code>false</code> otherwise.
     * \return <code>true</code> if updaing all the models has been successful,
     *     sets error and returns <code>false</code> otherwise.
     */
    bool modelUpdate(long time,bool &anyUpdated);
    
    /**
     * \brief Adds a request to be run after the next model update.
     * \param request The request.
     */
    void addRequest(EngineRequest *request);
    
    /**
     * \brief Processes the pending engine request.   
     * \return <code>true</code> on success, sets error and returns
     *     <code>false</code> on error.
     */
    bool processRequests();
    
    /**
     * \brief Called on fatal errors.
     * \param msg The message.
     */
    void fatalError(const char *msg);
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_H