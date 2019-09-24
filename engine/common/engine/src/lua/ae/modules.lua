-- @brief Responsible for loading Lua modules.
local modules = {}

-- @brief Stores a module in the globals.
-- @param modname The module name.
-- @param dst The destination object (table) given as string (will be created
--   if does not exist).
-- @param mod The module.
local function store(modname,dst,mod)
    -- get the table names
    local names = {}
    for name in string.gmatch(dst,'[^.]+') do
        ae.itable.append(names,name)
    end
    
    -- store the module directlty in the globals
    if #names == 1 then
        rawset(_G,names[1],mod)
        return
    end
    
    -- the first table in the globals
    local obj = _G[names[1]]
    if not obj then
        obj = {}
        rawset(_G,names[1],obj)
    end
    
    -- store in intermediate tables
    for index = 2,#names - 1 do
        local nextObj = obj[names[index]]
        if not nextObj then
            nextObj = {}
            obj[names[index]] = nextObj
        end
        obj = nextObj
    end
    
    -- store the module in top object
    if obj[names[#names]] then
        error('Attempt to overwrite ' .. dst ..
            ' when loading Lua module ' .. modname)
    end
    rawset(obj,names[#names],mod)
end

-- @func
-- @brief Loads a module.
-- @param modname The module name.
-- @func
-- @brief Loads a module.
-- @param modname The module name.
-- @param dst The destination object (table) given as string (will be created
--   if does not exist).
function modules.load(modname,dst)
    -- do nothing if alread loaded
    if package.loaded[name] then
        return
    end

    -- set destination if not given
    if not dst then
        dst = modname
    end

    -- load, store
    local mod = require(modname)
    store(modname,dst,mod)
end

-- @brief Loads a number of modules.
-- @param mods The modules given as table (integer indexes) of strings
--   (module names) or table of tables with name and destination object (table).
function modules.loadAll(mods)
    for _,mod in ipairs(mods) do
        if ae.types.isstring(mod) then
            modules.load(mod)
        else
            modules.load(mod.name,mod.dst)
        end
    end
end

-- @brief Loads the core modules.
function modules.loadCore()
    if not ae then
        ae = {}
    end
    
    ae.log = require('ae.log')
    ae.string = require('ae.string')
    ae.types = require('ae.types')
    ae.math = require('ae.math')
    ae.itable = require('ae.itable')
    ae.table = require('ae.table')
    ae.stack = require('ae.stack')
    ae.oo = require('ae.oo')
    ae.display = require('ae.display')
    ae.size = require('ae.size')
end

-- @brief Gets the modules to load.
-- @return The modules as table.
function modules.get()
    return {
        'ae.uv',
        'ae.state',
        
        'ae.event.SingleTouchProxy',
        'ae.event.DefaultTouchHandler',
        
        'ae.texture.TextureResource',
        'ae.texture.FileTextureResource',
        'ae.texture.TextureLoader',
        'ae.font.Font',
        'ae.font.FontResource',
        'ae.font.FontLoader',
        'ae.audio.SoundResource',
        'ae.audio.SoundLoader',
        'ae.loader.ResourceLoader',
        'ae.loader.Loader',
        'ae.loader.DelayResource',
        'ae.loader.DelayLoader',
        'ae.loader.LuaModuleLoader',
        'ae.loader.LuaModuleResource',
        'ae.loader.FuncCallResource',
        'ae.loader.FuncCallLoader',
        'ae.loaders',
        
        'ae.animation.ease',
        'ae.animation.Player',
        'ae.animation.Chain'
    }
end

return modules