-- @module ui.gpu.drawers.Drawer
-- @group UI
-- @brief An abstract wrapper of a drawer.
local Drawer = ae.oo.class()
    
-- @brief Creates a Drawer object.
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
-- @return A new Drawer object. 
function Drawer.new(drawerCapacity,indexCapacity)
    local self = ae.oo.new(Drawer)
    Drawer.construct(self,drawerCapacity,indexCapacity)
    
    return self
end

-- @brief Constructs a Drawer object.
-- @param self The object. 
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
function Drawer.construct(self,drawerCapacity,indexCapacity)
    self.drawer = {
        capacity = drawerCapacity,
        drawer = self:createDrawer(drawerCapacity),
        position = 0
    }
    
    self.index = {
        capacity = indexCapacity,
        index = self:createIndex(indexCapacity),
        position = 0
    }
end

-- @brief Creates the drawer itself.
-- @param capacity The number of vertices supported by the drawer.
-- @return The created drawer.
function Drawer:createDrawer(capacity)
    error('Must be implemented in a subclass')
end

-- @brief Creates the drawer index.
-- @param capacity The number of vertices supported by the index.
-- @return The created drawer index.
function Drawer:createIndex(capacity)
    error('Must be implemented in a subclass')
end

-- @brief Raises an error if there is no active command.
function Drawer:chkCommand()
    if not self.command then
        error('No active command. Start drawing first.')
    end
end

-- @brief Reserves a number of vertices and increments the position.
-- @param count The number of vertices to reserve.
-- @return The position of the first reserved vertex.
function Drawer:reserveVertices(count)
    local first = self.drawer.position
    self.drawer.position = self.drawer.position + count
    
    -- if there is a command started, update the command...
    if self.command then
        if not self.command.firstVertex then
            self.command.firstVertex = first
        end
    end
    
    -- ...otherwise just return the position of the first reserved vertex
    return first   
end

-- @brief Reserves a number of indices and increments the position.
-- @param count The number of indices to reserve.
-- @return The position of the first reserved index.
function Drawer:reserveIndices(count)
    local first = self.index.position
    self.index.position = self.index.position + count
    
    -- if there is a command started, update the command...
    if self.command then
        if not self.command.firstIndex then
            self.command.firstIndex = first
        end
    end
        
    -- ...otherwise just return the position of the first reserved index
    return first    
end

-- @brief Resets the vertex and index position.
function Drawer:reset()
    self.drawer.position = 0
    self.index.position = 0
end

-- @brief Sets an index value.
-- @param position The index position.
-- @param value The index value.
function Drawer:setIndex(position,value)
    self.index.index:setValue(position,value)
end

-- @brief Sets index values.
-- @param first The position of the first index to set.
-- @param values The values.
function Drawer:setIndices(first,values)
    for index = 1,#values do
        self.index.index:setValue(first + index - 1,values[index])
    end
    
end

-- @brief Creates an empty command.
-- @param values The initial command values.
-- @return The created command.
function Drawer:createCommand(values)
    self.command = ui.gpu.drawers.DrawerCommand.new()
    for key,value in pairs(values) do
        self.command[key] = value
    end
    return self.command
end

-- @brief Appends components to the drawer.
-- @param components The table of components.
function Drawer:appendComponents(components)
    error('Must be implemented in a subclass')
end

-- @brief Appends a component to the drawer.
-- @param component The component.
function Drawer:appendComponent(component)
    self:appendComponents({component})
end

-- @func
-- @brief Starts building a draw command.
-- @func
-- @brief Starts building a draw command.
-- @param transformation The transformation matrix.
function Drawer:start(transformation)
    error('Must be implemented in a subclass')
end

-- @brief Enqueues the draw command.
-- @return The command
function Drawer:enqueue()
    local cmd = self.command
    self.command = nil

    -- raise an error if no vertex has been appended
    if not cmd.firstVertex then
        error('No vertex appended')
    end

    -- counts
    cmd.vertexCount = self.drawer.position - cmd.firstVertex
    if cmd.firstIndex then
        cmd.indexCount = self.index.position - cmd.firstIndex
    end
    
    -- enqueue
    ui.gpu.queue.enqueue(cmd)
    return cmd
end

-- @func
-- @brief Enqueues components.
-- @full Enqueues components by starting a draw command, appending the
--   components to the drawer and enqueuing the draw command.
-- @param components The components.
-- @param transformation The transformation.
-- @func
-- @brief Enqueues components.
-- @full Enqueues components by starting a draw command, appending the
--   components to the drawer and enqueuing the draw command.
-- @param components The components.
function Drawer:enqueueComponents(components,transformation)
    self:start(transformation)
    self:appendComponents(components)
    return self:enqueue()
end

-- @func
-- @brief Enqueues a component.
-- @full Enqueues a component by starting a draw command, appending the
--   component to the drawer and enqueuing the draw command.
-- @param component The component.
-- @param transformation The transformation.
-- @func
-- @brief Enqueues a component.
-- @full Enqueues a component by starting a draw command, appending the
--   component to the drawer and enqueuing the draw command.
-- @param component The component.
function Drawer:enqueueComponent(component,transformation)
    return self:enqueueComponents({component},transformation)
end

return Drawer