-- @module ae.LuaModuleLoader
-- @brief Responsible for loading Lua modules using ()[ae.ResourceLoader].
local LuaModuleLoader = ae.oo.class()
    
-- @brief Creates a LuaModuleLoader object.
-- @param resourceLoader The resource loader.
-- @return A new LuaModuleLoader object. 
function LuaModuleLoader.new(resourceLoader)
    local self = ae.oo.new(LuaModuleLoader)
    LuaModuleLoader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a LuaModuleLoader object.
-- @param self The object. 
-- @param resourceLoader The resource loader.
function LuaModuleLoader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
end

-- @brief Loads a Lua module.
-- @func
-- @param name The module name.
-- @func
-- @param name The module name.
-- @param dst The destination object (table) given as string (will be created
--   if does not exist).
function LuaModuleLoader:load(name,dst)
    -- do nothing if alread loaded
    if package.loaded[name] then
        return
    end
    local id = name
    
    -- check if already exists
    local luaModuleResource = self.resourceLoader:get(id)
    if luaModuleResource then
        return luaModuleResource
    end
    
    -- create and add the Lua source resource
    luaModuleResource = ae.loader.LuaModuleResource.new(name,dst)
    self.resourceLoader:add(luaModuleResource)
    
    return luaSourceResource
end

-- @brief Loads a number of modules.
-- @param modules The modules given as table (integer indexes) of strings
--   (module names) or table of tables with name and destination object (table).
function LuaModuleLoader:loadAll(modules)
    for _,mod in ipairs(modules) do
        if ae.types.isstring(mod) then
            self:load(mod)
        else
            self:load(mod.name,mod.dst)
        end
    end
end

return LuaModuleLoader