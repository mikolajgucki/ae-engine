-- @group UI
-- @brief Wraps vertex buffer data to provide easy-to-use functions to append
--   vertices and rectangles.
local ColorDrawerBuffer = ae.oo.class()

-- @func
-- @brief Creates a drawer buffer with index capacity the same as the
--   vertex capacity.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @return The drawer buffer.
-- @func
-- @brief Creates a drawer buffer.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @param indexCapacity The maximal number of indices the buffer can store.
-- @return The drawer buffer.
function ColorDrawerBuffer.new(vertexCapacity,indexCapacity)
    local self = ae.oo.new(ColorDrawerBuffer)
    ColorDrawerBuffer.construct(self,vertexCapacity,indexCapacity)
    
    return self
end

-- @func
-- @brief Constructs a drawer buffer with index capacity the same as the
--   vertex capacity.
-- @param self The drawer buffer object.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @func
-- @brief Constructs a drawer buffer.
-- @param self The drawer buffer object.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @param indexCapacity The maximal number of indices the buffer can store.
--   Defaults to `vertexCapacity`.
function ColorDrawerBuffer.construct(self,vertexCapacity,indexCapacity)
    indexCapacity = indexCapacity or vertexCapacity
    
    -- vertex data type and offset
    self.types = {
        coords = ae.gl.GL_FLOAT
    }
    self.offsets = {
        coords = 0
    }
    
    -- coordinates data
    self.coordsStride = ae.gl.GLfloatSize * 2
    self.coordsVbd = ae.gl.VertexBufferData.new(
        self.coordsStride,vertexCapacity)
    self.vertexIndex = 0
        
    -- index data
    self.indexVbd = ae.gl.VertexBufferData.new(ae.gl.GLushortSize,indexCapacity)
    self.indexIndex = 0        
end

-- @func
-- @brief Appends indices to the buffer.
-- @param indices The indices to append.
-- @func
-- @brief Appends indices to the buffer.
-- @param indices The indices to append.
-- @param shift The value to add to each index.
function ColorDrawerBuffer:appendIndices(indices,shift)
    shift = shift or 0
    for _,index in ipairs(indices) do
        self.indexVbd:set1us(self.indexIndex,0,index + shift)
        self.indexIndex = self.indexIndex + 1
    end
end

-- @brief Appends a vertex.
-- @param x The vertex X coordinate.
-- @param y The vertex Y coordinate.
function ColorDrawerBuffer:appendVertex(x,y)
    self.coordsVbd:set2f(self.vertexIndex,self.offsets.coords,x,y)
    self.vertexIndex = self.vertexIndex + 1
end

-- @func
-- @brief Moves the indexes to the beginning of the buffer.
-- @full Moves the indexes to the beginning of the buffer. The next appended
--   indices/vertices will be start from the buffer beginning.
function ColorDrawerBuffer:rewind()
    self.indexIndex = 0
    self.vertexIndex = 0
end

-- @func
-- @brief Appends a rectangle to the buffer.
-- @full Appends a rectangle to the buffer. A rectangle takes 6 indices and
--   4 vertices.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The rectangle width.
-- @param height The rectangle height.
function ColorDrawerBuffer:appendRect(x,y,width,height)
    self:appendIndices({0,1,2, 0,2,3},self.vertexIndex)
    local vertexIndex = self.vertexIndex
    
    self:appendVertex(x,y + height)
    self:appendVertex(x + width,y + height)
    self:appendVertex(x + width,y)
    self:appendVertex(x,y)
    
    return vertexIndex
end

return ColorDrawerBuffer