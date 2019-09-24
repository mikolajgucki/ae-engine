-- cannot use require here as searcher has not yet been set
ae = ae or {}
ae.init = ae.load('ae/init.lua')

-- @brief Initializes the key handler.
local function initKeyHandler()
    ae.key = {}
    ae.key.down = function(keyName,keyCode)
        -- TODO Handle key down event.
    end
    
    ae.key.up = function(keyName,keyCode)
        -- TODO Handle key up event.
    end    
end

-- @brief Initializes the touch handler.
local function initTouchHandler()
    ae.touch = {}    
    ae.touch.down = function(pointerId,x,y)
        -- TODO Handle touch down event.
    end
    
    ae.touch.move = function(pointerId,x,y)
        -- TODO Handle touch move event.
    end
    
    ae.touch.up = function(pointerId,x,y)
        -- TODO Handle touch up event.
    end
end

-- @brief Called when the engine has been initialized.
function ae.ready()
    ae.log.trace('ae.ready()')
    
    initKeyHandler()
    initTouchHandler()    
    
    -- TODO Launch the game.
end

-- @brief Called when the engine is about to pause.
function ae.pausing()
    ae.log.trace('ae.pausing()')
    -- TODO Store game state.
    -- TODO Pause music.
end

-- @brief Called when the engine has just resumed.
function ae.resuming()
    ae.log.trace('ae.resuming()')
    -- TODO Restore game state.
    -- TODO Resume music.
end

-- @brief Called when the game is about to terminate.
function ae.terminating()
    ae.log.trace('ae.terminating()')
    -- TODO Store game state.
end