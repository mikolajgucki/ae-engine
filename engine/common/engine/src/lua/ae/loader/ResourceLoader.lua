-- @brief Provides handy way of loading resources.
local ResourceLoader = ae.oo.class()

-- @brief Creates a resource loader.
-- @return The empty resource loader.
function ResourceLoader.new()
    local self = ae.oo.new(ResourceLoader)
    ResourceLoader.construct(self)
    
    return self
end

-- @brief Constructs a resource loader.
-- @param self The resource loader object.
function ResourceLoader.construct(self)
    self.resources = {}
end

-- @brief Adds a resource.
-- @full Adds a resource. Fails if a resource of the identifier already exists.
-- @param resource The resource.
function ResourceLoader:add(resource)
    local id = resource.id
    if not id then
        error('Attempt to add a resource without identifier')
    end
    
    -- on-display loading?
    if self.onDisplayLoading then
        resource.onDisplay = true
    end
    
    if not self.resourcesToLoad then
        if self.resources[id] then
            error('Attempt to add a resource of the same identifier ' ..
                tostring(id))
        end
        
        resource:load()
        self.resources[id] = resource
    else
        if self.resourcesToLoad[id] then
            error('Attempt to add a resource of the same identifier' ..
                tostring(id))
        end
        
        -- the index which determines the load order
        resource.loadIndex = ae.table.size(self.resourcesToLoad)
        
        -- add
        self.resourcesToLoad[id] = resource
    end
end

-- @brief Gets a resource of an identifier.
-- @param id The resource identifier.
-- @return The resource if contained or `nil` otherwise.
function ResourceLoader:get(id)
    if self.resourcesToLoad then
        -- if the resource is to be loaded
        if self.resourcesToLoad[id] then
            return self.resourcesToLoad[id]
        end
        
        -- if the resource is already loaded
        if self.resources[id] then
            -- let it be marked as to be loaded not to delete it
            self.resourcesToLoad[id] = self.resources[id]
            return self.resources[id]
        end
        
        return nil
    end
    
    -- on-display loading?
    local resource = self.resources[id]
    if resource then
        resource.onDisplay = self.onDisplayLoading
    end
    
    return self.resources[id]
end

-- @brief Deletes a resource.
-- @param resource The resource to delete.
function ResourceLoader:delete(resource)
    if resource.delete then
        resource:delete()
    end
    self.resources[resource.id] = nil
end

-- @brief Enters the on-display loading mode.
-- @full Enters the on-display loading mode. That is, the mode in which the
--   resources which are displayed while the proper loading takes place
--   are loaded. All the resources loaded in this mode will be deleted after
--   all the resources have been loaded.
function ResourceLoader:enterOnDisplayLoading()
    self.onDisplayLoading = true
end

-- @brief Enters the loading mode.
-- @full Enters the loading mode in which loading of the resources itself is
--   delayed till exit of the loading mode.
function ResourceLoader:enterLoading()
    self.resourcesToLoad = {}
    self.onDisplayLoading = nil
end

-- @brief Gets the actions which delete the unnecessary resources.
-- @param self The resource loader object.
-- @return The delete actions.
local function getDeleteActions(self)
    local actions = {}
    local resourceLoader = self

    -- for each resource
    for id,resource in pairs(self.resources) do
        if not self.resourcesToLoad[id] and not resource.onDisplay then
            local deleteAction = {
                run = function()
                    resourceLoader:delete(resource)
                end
            }
            ae.itable.append(actions,deleteAction)
        end
    end
    
    return actions
end

-- @brief Gets the actions which load the requested resources.
-- @param self The resource loader objec.
-- @return The load actions.
local function getLoadActions(self)
    local actions = {}
    local resourceLoader = self

    -- for each resource to load
    for id,resource in pairs(self.resourcesToLoad) do
        if not self.resources[id] and self.resourcesToLoad[id] then
            local loadAction = {
                loadIndex = resource.loadIndex,
                run = function()
                    -- adding will also load the resource itself
                    resourceLoader:add(resource)
                end
            }
            ae.itable.append(actions,loadAction)
        end
    end
    
    -- sort by load index
    table.sort(actions,function(a,b)
        return a.loadIndex < b.loadIndex
    end)
    
    return actions
end

-- @brief Gets the on-display resources that are unnecessary and need to be
--   deleted.
-- @param self The resource loader object.
-- @return The on-display resources to delete.
local function getOnDisplayResourcesToDelete(self)
    local resources = {}
    for id,resource in pairs(self.resources) do
        if not self.resourcesToLoad[id] and resource.onDisplay then
            ae.itable.append(resources,resource)
        end
    end
    
    return resources
end

-- @func
-- @brief Exits the loading mode.
-- @full Exits the loading mode. Performs the reconciliation between the
--   resources already loaded when entering the loading mode and the resources
--   to be loaded.
-- @func
-- @brief Exits the loading mode.
-- @full Exits the loading mode. Performs the reconciliation between the
--   resources already loaded when entering the loading mode and the resources
--   to be loaded.
-- @param progressFunc The progress function taking 2 arguments: number of
--   already loaded resources and total number of resources.
function ResourceLoader:exitLoading(progressFunc)
    self.progressFunc = progressFunc
    self.actions = {} -- performed in the update function one by one
    local resourceLoader = self
    
    -- resources to delete and load
    ae.itable.appendAll(self.actions,getDeleteActions(self))
    ae.itable.appendAll(self.actions,getLoadActions(self))
    
    if #self.actions == 0 then
        error('No resources to load or delete')
    end
    
    -- get the on-display resources to be deleted
    self.onDisplayResourcesToDelete = getOnDisplayResourcesToDelete(self)
    
    -- the resources will be loaded in the load actions
    self.resourcesToLoad = nil
end

-- @brief Called when all the resource have been loaded.
-- @param self The resource loader object.
local function loaded(self)
    -- delete the on-display resources
    if not ae.itable.empty(self.onDisplayResourcesToDelete) then
        local resourceLoader = self
        ae.itable.each(self.onDisplayResourcesToDelete,function(resource)
            resourceLoader:delete(resource)
        end)
        self.onDisplayResourcesToDelete = nil
    end 
    
    -- clean up the on-display flags
    for _,resource in pairs(self.resources) do
        resource.onDisplay = nil
    end
end

-- @brief Performs all the update actions.
-- @full Performs all the update actions. 
-- TODO Details on :update()
function ResourceLoader:update()
    if self.actions then
        local run = nil
        local runIndex = nil
        
        -- find the next action to run
        for index,action in ipairs(self.actions) do
            if self.actions[index].run then
                runIndex = index
                run = self.actions[index].run
                self.actions[index].run = nil
                break
            end
        end
        
        if run then
            run()
            if self.progressFunc then
                self.progressFunc(runIndex,#self.actions)
            end
            if runIndex == #self.actions then
                loaded(self)
            end
        else
            self.actions = nil
            self.progressFunc = nil
        end
    end
end

return ResourceLoader