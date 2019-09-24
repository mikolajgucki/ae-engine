-- @group UI
-- @brief A texture drawer wrapper.
local TexDrawer,super = ae.oo.subclass(ui.gpu.drawers.Drawer)
    
-- @brief Creates a TexDrawer object.
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
-- @return A new TexDrawer object. 
function TexDrawer.new(drawerCapacity,indexCapacity)
    local self = ae.oo.new(TexDrawer)
    TexDrawer.construct(self,drawerCapacity,indexCapacity)
    
    return self
end

-- @brief Constructs a TexDrawer object.
-- @param self The object. 
-- @param drawerCapacity The number of vertices supported by the drawer.
-- @param indexCapacity The number of vertices supported by the index.
function TexDrawer.construct(self,drawerCapacity,indexCapacity)
    super.construct(self,drawerCapacity,indexCapacity)
end

-- @brief Creates the drawer itself.
-- @param capacity The number of vertices supported by the drawer.
-- @return The created drawer.
function TexDrawer:createDrawer(capacity)
    local drawer = gpu.DefaultDrawer.new('texture',capacity)
    
    -- keep the buffer on the GPU side
    drawer:moveToGPU()
    drawer:deleteCPUData()
    
    return drawer
end

-- @brief Creates the drawer index.
-- @param capacity The number of vertices supported by the index.
-- @return The created drawer index.
function TexDrawer:createIndex(capacity)
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
            cmd.index,cmd.texture,cmd.transformation)
    else
        cmd.drawer:enqueue(gpuQueue,cmd.firstVertex,cmd.vertexCount,nil,
            cmd.texture,cmd.transformation)
    end
end

-- @func
-- @brief Starts building a draw command.
-- @return The command.
-- @func
-- @brief Starts building a draw command.
-- @param transformation The transformation matrix.
-- @return The command.
function TexDrawer:start(transformation)
    local command = self:createCommand({
        transformation = transformation,
        drawer = self.drawer.drawer,
        index = self.index.index,
        execute = execute
    })
    
    return command
end

-- @brief Sets a vertex.
-- @param position The vertex position.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param u The U coordinate.
-- @param v The V coordinate.
function TexDrawer:setVertex(position,x,y,u,v)
    self.drawer.drawer:setCoords(position,x,y,u,v)
end

-- @brief Sets a rectangle.
-- @param position The position of the first vertex.
-- @param x The X coordinate of the lower-left corner.
-- @param y The Y coordinate of the lower-left corner.
-- @param width The rectangle width.
-- @param height The rectangle height.
-- @param subtexture The subtexture.
function TexDrawer:setRect(position,x,y,width,height,subtexture)
    local x1 = x + width
    local y1 = y + height

    local u0 = subtexture.u
    local v0 = subtexture.v
    local u1 = subtexture.u + subtexture.width
    local v1 = subtexture.v + subtexture.height

    self:setVertex(position,     x,y,   u0,v1)
    self:setVertex(position + 1, x1,y,  u1,v1)
    self:setVertex(position + 2, x1,y1, u1,v0)
    self:setVertex(position + 3, x,y1,  u0,v0)
end

-- @brief Appends components to the drawer.
-- @param components The table of components.
function TexDrawer:appendComponents(components)
    self:chkCommand()
    local command = self.command
    
    -- for each component
    ae.itable.each(components,function(component)
        if command.texture and command.texture ~= component:getTexture() then
            error('Attempt to append a component with different texture')
        end
        command.texture = component:getTexture()
        
        if component.appendToDrawer then
            component:appendToDrawer(self)
        end
    end)
end

return TexDrawer