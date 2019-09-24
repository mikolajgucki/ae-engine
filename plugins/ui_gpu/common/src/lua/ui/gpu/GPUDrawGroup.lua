-- @module ui.gpu.GPUDrawGroup
-- @group UI
-- @brief Represents a group of components transformed by a single
--   transformation matrix.
local GPUDrawGroup = ae.oo.class()
    
-- @brief Creates a GPUDrawGroup object.
-- @param drawer The drawer ()[ui.gpu.drawers.Drawer] associated with
--   this group.
-- @return A new GPUDrawGroup object. 
function GPUDrawGroup.new(drawer)
    local self = ae.oo.new(GPUDrawGroup)
    GPUDrawGroup.construct(self,drawer)
    
    return self
end

-- @brief Constructs a GPUDrawGroup object.
-- @param drawer The drawer ()[ui.gpu.drawers.Drawer] associated with
--   this group.
-- @param self The object. 
function GPUDrawGroup.construct(self,drawer)
    -- drawer
    self.drawer = drawer
    
    -- components belonging to the group
    self.components = {}
    
    -- transformation matrix
    self.transformation = Mat4.new()
    self.transformation:identity()
end

-- @brief Called when the transformation matrix has been changed.
function GPUDrawGroup:transformationChanged()
end

-- @brief Adds a component to the group.
-- @param component The component.
function GPUDrawGroup:addComponent(component)
    ae.itable.append(self.components,component)
    local group = self
    
    -- transformation function
    component.transformPoint = function(self,p)
        local v = Vec3.new(p.x,p.y)
        local r = Vec3.new()
        
        group.transformation:mulVec3(v,r)
        return {
            x = r:x(),
            y = r:y()
        }
    end
end

-- @brief Adds components to the group.
-- @param components The components.
function GPUDrawGroup:addComponents(components)
    ae.itable.each(components,function(component)
        self:addComponent(component)
    end)
end

-- @brief Adds a number of components from a container.
-- @param container The container.
-- @param ids The (integer) table with identifiers of the components to add.
function GPUDrawGroup:addComponentsFromContainer(container,ids)
    ae.itable.each(ids,function(id)
        self:addComponent(container:find(id))
    end)
end

-- @brief Removes all the components.
function GPUDrawGroup:clear()
    self.components = {}
end

-- @brief Appends the components to the drawer.
-- @return The drawer command.
function GPUDrawGroup:appendToDrawer()
    self.drawer:start(self.transformation)
    self.drawer:appendComponents(self.components)
    return self.drawer:enqueue()
end

-- @brief Appends the components to the drawer and removes them from the group.
function GPUDrawGroup:flushToDrawer()
    self:appendToDrawer()
    self:clear()
end

-- @brief Builds a number of draw groups from a container (layout).
-- @param container The container (layout) from which to pull components.
-- @param groups The group table in which the key is a draw group identifier
--   and value is the table of components belonging to the group.
function GPUDrawGroup.buildFromContainer(container,groups)
    local drawGroups = {}
    
    for _,group in pairs(groups) do
        local drawGroup = GPUDrawGroup.new(group.drawer)
        drawGroup.id = group.id
        ae.itable.append(drawGroups,drawGroup)
        
        for _,id in ipairs(group.ids) do
            drawGroup:addComponent(container:find(id))
        end
    end
    
    return drawGroups
end

return GPUDrawGroup