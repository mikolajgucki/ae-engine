local loaders = {}

-- @brief Initializes the loaders.
local function init()
    -- @var 
    -- @name $modname.resources
    -- @brief The resource loader.
    loaders.resources = ae.loader.ResourceLoader.new()
    
    -- @var
    -- @name $modname.delay
    -- @brief The delay loader.
    loaders.delay = ae.loader.DelayLoader.new(loaders.resources)
    
    -- @var
    -- @name $modname.modules
    -- @brief The Lua module loader.
    loaders.modules = ae.loader.LuaModuleLoader.new(loaders.resources)
    
    -- @var
    -- @name $modname.calls
    -- @brief The function call loader.
    loaders.calls = ae.loader.FuncCallLoader.new(loaders.resources)
    
    -- @var
    -- @name $modname.textures
    -- @brief The texture loader.
    loaders.textures = ae.texture.TextureLoader.new(loaders.resources)
    
    -- @var
    -- @name $modname.fonts
    -- @brief The font loader.
    loaders.fonts = ae.font.FontLoader.new(loaders.resources)
    
    -- @var
    -- @name $modname.sounds
    -- @brief The sound loader.
    loaders.sounds = ae.audio.SoundLoader.new(loaders.resources)
end

-- initialize
init()

return loaders