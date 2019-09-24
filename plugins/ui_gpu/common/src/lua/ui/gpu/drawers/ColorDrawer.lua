-- @group UI
-- @brief A color drawer wrapper.
local ColorDrawer,super = ae.oo.subclass(ui.gpu.drawers.Drawer)
    
-- @brief Creates a ColorDrawer object.
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
-- @return A new ColorDrawer object. 
function ColorDrawer.new(drawerCapacity,indexCapacity)
    local self = ae.oo.new(ColorDrawer)
    ColorDrawer.construct(self,drawerCapacity,indexCapacity)
    
    return self
end

-- @brief Constructs a ColorDrawer object.
-- @param self The object. 
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
function ColorDrawer.construct(self,drawerCapacity,indexCapacity)
    super.construct(self,drawerCapacity,indexCapacity)
end

-- @brief Creates the drawer itself.
-- @param capacity The number of vertices supported by the drawer.
-- @return The created drawer.
function ColorDrawer:createDrawer(capacity)
    local drawer = gpu.DefaultDrawer.new('vertex.color',capacity)
    
    -- keep the buffer on the GPU side
    drawer:moveToGPU()
    drawer:deleteCPUData()
    
    return drawer
end

-- @brief Creates the drawer index.
-- @param capacity The number of vertices supported by the index.
-- @return The created drawer index.
function ColorDrawer:createIndex(capacity)
    return gpu.DrawerIndex.new(capacity)
end

-- @brief Executes a command.
-- @param gpuQueue The GPU queue.
-- @param cmd The command.
local function execute(gpuQueue,cmd)
    if not cmd.visible then
        return
    end

    if cmd.indexCount then
        cmd.drawer:enqueue(gpuQueue,cmd.firstIndex,cmd.indexCount,
            cmd.index,nil,cmd.transformation)
    else
        cmd.drawer:enqueue(gpuQueue,cmd.firstVertex,cmd.vertexCount,nil,
            nil,cmd.transformation)
    end
end

-- @func
-- @brief Starts building a draw command.
-- @return The command.
-- @func
-- @brief Starts building a draw command.
-- @param transformation The transformation matrix.
-- @return The command.
function ColorDrawer:start(transformation)
    local command = self:createCommand({
        transformation = transformation,
        drawer = self.drawer.drawer,
        index = self.index.index,
        execute = execute
    })
    
    return command
end

-- @func
-- @brief Sets a vertex.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @func
-- @brief Sets a vertex.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function ColorDrawer:setVertex(position,x,y,r,g,b,a)
    self.drawer.drawer:setVertexCoords(position,x,y)
    self.drawer.drawer:setVertexColor(position,r,g,b,a or 1)
end

-- @brief Appends components to the drawer.
-- @param components The table of components.
function ColorDrawer:appendComponents(components)
    self:chkCommand()
    
    -- for each component
    ae.itable.each(components,function(component)    
        if component.appendToDrawer then
            component:appendToDrawer(self)
        end        
    end)
end

return ColorDrawer