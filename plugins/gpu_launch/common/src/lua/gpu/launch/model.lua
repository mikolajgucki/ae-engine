-- @brief The GPU Launch model.
local model = {}

-- The texture
local texture

-- The color drawer.
local colorDrawer

-- The texture drawer.
local texDrawer

-- The GPU queue.
local gpuQueue

-- @brief Creates a color drawer.
-- @param color The color.
local function createColorDrawer(color)
    -- create drawer
    colorDrawer = gpu.DefaultDrawer.new('color',6)
    
    -- color
    colorDrawer:setColor(color.r,color.g,color.b,color.a)
    
    -- vertices
    colorDrawer:setVertexCoords(0,-1,-1)
    colorDrawer:setVertexCoords(1, 1,-1)
    colorDrawer:setVertexCoords(2,-1, 1)
    
    colorDrawer:setVertexCoords(3, 1, 1)
    colorDrawer:setVertexCoords(4, 1,-1)
    colorDrawer:setVertexCoords(5,-1, 1)
end

-- @brief Creates a texture drawer.
-- @param subtexture The subtexture to display.
-- @param size The size given as factor of the display.
local function createTexDrawer(subtexture,size)
    -- create drawer
    ae.log.trace('creating drawer')
    texDrawer = gpu.DefaultDrawer.new('texture',6)
    ae.log.trace('drawer created')
    
    -- size
    local width = gpu.bounds.width * size
    local height = ae.size.getHeight(width,subtexture.aspect)
    
    -- center
    local x = gpu.bounds.x + (gpu.bounds.width - width) / 2
    local y = gpu.bounds.y + (gpu.bounds.height - height) / 2
    
    -- append rectangle
    gpu.launch.drawer.appendTexRect(texDrawer,x,y,width,height,subtexture)
end

-- @brief Initializes the launch image model.
-- @param color The background color.
-- @param subtexture The subtexture to display.
-- @param size The size given as factor of the display.
function model.init(color,subtexture,size)
    -- texture
    texture = Texture.load(subtexture.filename)
    
    -- drawer
    createColorDrawer(color)
    createTexDrawer(subtexture,size)
    
    -- queue
    gpuQueue = gpu.GPUQueue.new()
end

-- @brief Destorys the launch image model.
function model.destroy()
    if texture then
        texture:delete()
    end
        
    colorDrawer = nil
    texDrawer = nil
    gpuQueue = nil
    texture = nil
    
    collectgarbage()
end

-- @brief Draws the launch image.
function model.draw()
    colorDrawer:enqueue(gpuQueue,0,6)
    texDrawer:enqueue(gpuQueue,0,6,nil,texture)
    gpuQueue:flush()
end

-- @brief Updates the launch image model.
-- @param time The time elapsed since the last frame.
function model.update(time)
    if gpu.launch.runFunc then
        gpu.launch.runFunc()
    end
end

return model