-- @brief Initializes the engine.
-- @full Initializes the engine. The engine loads the module `root.lua` on
--   start. `ae.init` must be loaded from the root module.
local init = {}

-- @brief Table with a single item of key equal to the name of the OS
--   under which the engine is running.
ae.os = {}

-- @brief The key code table.
ae.keys = nil

-- @brief Reports an error.
-- @param msg The message.
-- @param ... The format values.
function ae.error(msg,...)
    error(string.format(msg,...))
end

-- @brief Tests if a string ends with a suffix.
-- @param str The string.
-- @param suffix The suffix.
-- @return `true` if the string ends with the suffix, `false` otherwise.
local function endsWith(str,suffix)
    return str:sub(-suffix:len()) == suffix
end

-- @brief Loads a chunk.
-- @param filename The name of the file from which to load the source.
local function load(filename)
    if endsWith(filename,'.lua') or string.find(filename,'/') then
        return ae.load(filename)
    end

    return ae.load(filename:gsub('%.','/') .. '.lua')
end

-- @brief The packer loader.
-- @param filename The name of the file from which to load the module.
local function packageLoader(filename)
    return load
end

-- @brief Adds the package loader which uses `ae.load` to load modules.
local function initPackageLoader()
    table.insert(package.searchers,2,packageLoader)
end

-- @brief Initializes the table `ae.os`.
local function initOS()
    local name = ae.getOS()
    ae.os[name] = true
    
    -- android
    if android then
        -- SDK
        if android.getSDK then
            ae.os.sdk = android.getSDK()
        else
            ae.os.sdk = -1
        end
        ae.log.trace('Android SDK is %d',ae.os.sdk)
    end
end

-- @brief Initializes the keys.
local function initKeys()
    ae.keys = ae.getKeyCodeTable()
    ae.log.trace('Key code table is:')
    for name,code in pairs(ae.keys) do
        ae.log.trace("  ae.keys['" .. name .. "'] = " .. tostring(code))
    end
end

-- @brief Runs the initializer (called from C++).
-- @full Runs the initializer (called from C++). The function `ae.ready` is
--   called when the engine has been initialized.
function init.run()
    log.trace('ae.init.run() called')
    local startTime = ae.getTicks()
    
    ae.modules = require('ae.modules')
    ae.modules.loadCore()
    
    -- assets additional functions
    if assets then
        ae.modules.load('ae.assets')
    end
    
    -- load the remaining modules
    ae.modules.loadAll(ae.modules.get())
    
    -- log Lua sources load time
    local loadTime = ae.getTicks() - startTime
    ae.log.trace('Default Lua sources loaded in %dms',loadTime)
    
    initOS()
    initKeys()
    ae.init.model = ae.prependModel(nil,'ae.init.update')
    
    -- let the project have a chance to initialize
    if ae.initProject then
        ae.initProject()
    end    
end

-- @brief Invoked as the first model update.
-- @full Invoked as the first model update which initializes the engine
--   after OpenGL is set up.
function init.update()
    ae.log.trace('ae.init.update() called')
    
    ae.removeModel(ae.init.model)
    ae.init.model = nil
    
    if ae.ready then
        ae.ready()
    end
end

-- @brief Initializes the Lua stuff.
local function initLua()
    log.trace('initLua() called')
    log.trace('Lua version is ' .. _VERSION)

    -- loader
    initPackageLoader()
end

-- initialize Lua
initLua()

return init
