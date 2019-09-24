-- @brief Provides shortcut functions for building, updating with progress
--   and finishing resource loading.
-- @full Subclasses must implement the following methods:
--   * `init()` called to create the resources and UI elements which display
--      the actual progress of loading the resources.
--   * `create()` called to create the resources to be loaded. 
--   * `progressed(count,total)` called when loading of the resources has
--      progressed. `count` is the number of resources loaded so far. `total`
--      is the total number of resources to load.
--   * `loaded()` called when the resources have been loaded.
local Loader = ae.oo.class()

-- The loader states.
local states = {
    -- All the unnecessary data should be removed and the loading
    -- user interface should be created.
    init = 'init',
    
    -- The loaded scene and user interface are created which the resources
    -- are only requested to be loaded.
    create = 'create',
    
    -- The resources are being loaded.
    loading = 'loading'
}

-- @brief Creates a new loader.
-- @param resourceLoader The resource loader.
-- @return The new loader.
function Loader.new(resourceLoader)
    local self = ae.oo.new(Loader)
    Loader.construct(self,resourceLoader)
    
    return self
end

-- @brief Constructs a loader.
-- @param self The loader object.
-- @param resourceLoader The resource loader.
function Loader.construct(self,resourceLoader)
    self.resourceLoader = resourceLoader
    self.state = states.init
end

-- @brief Called when loading has progressed.
-- @param self The loader object.
-- @param count The number of resources loaded so far.
-- @param total The total number of resources to load.
local function loadProgressed(self,count,total)
    self:progressed(count,total)
    if count == total then
        self:loaded()
    end
end

-- @brief The update function. 
function Loader:update()
    self.resourceLoader:update()

    -- init
    if self.state == states.init then
        self.resourceLoader:enterOnDisplayLoading()
        self:init()
        
        self.state = states.create
        return
    end
    
    -- create
    if self.state == states.create then
        self.resourceLoader:enterLoading()
        self:create()        
        
        local loader = self
        self.resourceLoader:exitLoading(function(count,total)
            loadProgressed(loader,count,total)
        end)
        
        self.state = states.loading
        return
    end
end

-- @brief Gets a string which represents the loader.
-- @return The string representing the loader.
function Loader:__tostring()
    return ae.oo.tostring('ae.Loader',
        'state=' .. tostring(self.state),super.__tostring(self))
end

return Loader
