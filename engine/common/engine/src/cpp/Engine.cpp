#include <algorithm>
#include "lua.hpp"
#include "Log.h"
#include "DummyEngineMutexFactory.h"
#include "Engine.h"

using namespace std;

namespace ae {
    
namespace engine {

/// The log tag.
const char *logTag = "engine";
    
/** */
Engine::Engine(EngineCfg cfg_):Error(),cfg(cfg_),width(0),height(0),models(),
    requests() {
//
    create();
}
 
/** */
void Engine::create() {
// requests mutex
    requestsMutex = cfg.getMutexFactory()->createMutex();
    if (requestsMutex == (EngineMutex *)0) {
        setError(cfg.getMutexFactory()->getError());
        return;
    }
}

/** */
void Engine::start() {
}

/** */
void Engine::stop() {
}    
 
/** */
void Engine::displayResized(int _width,int _height) {
    width = _width;
    height = _height;
}

/** */
void Engine::prependModel(Model *model) {
    models.insert(models.begin(),model);
}

/** */
void Engine::appendModel(Model *model) {
    models.push_back(model);
}

/** */
void Engine::removeModel(Model *model) {
    vector<Model *>::iterator itr = find(models.begin(),models.end(),model);
    if (itr != models.end()) {
        models.erase(itr);
    }
}

/** */
bool Engine::modelDraw() {
    vector<Model*>::iterator itr;
    for (itr = models.begin(); itr != models.end(); ++itr) {
        Model *model = *itr;
        
        model->clearError();
        model->modelDraw();
        if (model->chkError()) {
            setError(model->getError());
            return false;
        }
    }
    
    return true;
}

/** */
bool Engine::modelUpdate(long time,bool &anyUpdated) {
    anyUpdated = false;
    vector<Model*>::iterator itr;
    for (itr = models.begin(); itr != models.end(); ++itr) {
        Model *model = *itr;
    
        model->clearError();
        if (model->modelUpdate(time) == true) {
            anyUpdated = true;
        }
        if (model->chkError()) {
            setError(model->getError());
            return false;
        }
    }
    
    return true;
}

/** */
void Engine::addRequest(EngineRequest *request) {
    requestsMutex->lock();
    requests.push_back(request);
    requestsMutex->unlock();    
}

/** */
bool Engine::processRequests() {
    requestsMutex->lock();
// run and remove requests
    vector<EngineRequest *>::iterator itr;
    for (itr = requests.begin(); itr != requests.end();) {
        EngineRequest *request = *itr;
        bool success = request->run();
        
        if (request->doRemoveRequest()) {
            itr = requests.erase(itr);
        }
        else {
            ++itr;
        }
        
        if (success == false) {
            requestsMutex->unlock();
            setError(request->getError());
            return false;
        }
    }
    
    requestsMutex->unlock();
    return true;
}

/** */
void Engine::fatalError(const char *msg) {
    Log::error(logTag,msg);
// TODO Kill the application with the error.
}

} // namespace

} // namespace
