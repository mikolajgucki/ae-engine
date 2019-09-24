-- @group UI
-- @brief Wraps vertex buffer data to provide easy-to-use functions to append
--   vertices and rectangles.
local TexDrawerBuffer = ae.oo.class()

-- @func
-- @brief Creates a drawer buffer with index capacity the same as the
--   vertex capacity and without color support.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @return The drawer buffer.
-- @func
-- @brief Creates a drawer buffer without color support.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @param indexCapacity The maximal number of indices the buffer can store.
-- @return The drawer buffer.
-- @func
-- @brief Creates a drawer buffer.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @param indexCapacity The maximal number of indices the buffer can store.
-- @param colorSupport Indicates if the buffer is to support colors.
-- @return The drawer buffer.
function TexDrawerBuffer.new(vertexCapacity,indexCapacity,colorSupport)
    local self = ae.oo.new(TexDrawerBuffer)
    TexDrawerBuffer.construct(self,vertexCapacity,indexCapacity,colorSupport)
    
    return self
end

-- @func
-- @brief Constructs a drawer buffer.
-- @param self The drawer buffer object.
-- @param vertexCapacity The maximal number of vertices the buffer can store.
-- @param indexCapacity The maximal number of indices the buffer can store.
-- @param colorSupport Indicates if the buffer is to support colors.
function TexDrawerBuffer.construct(self,vertexCapacity,indexCapacity,
    colorSupport)
--
    indexCapacity = indexCapacity or vertexCapacity
    
    -- vertex data types
    self.types = {
        coords = ae.gl.GL_FLOAT,
        texCoords = ae.gl.GL_UNSIGNED_SHORT
    }
    if colorSupport then
        self.types.color = ae.gl.GL_UNSIGNED_BYTE
    end
    
    -- vertex data sizes
    self.sizes = {
        coords = ae.gl.GLfloatSize * 2, -- xy
        texCoords = ae.gl.GLfloatSize * 2 -- uv
    }
    if colorSupport then
        self.sizes.color = ae.gl.GLubyteSize * 4 -- rgba
    end
    
    -- vertex data offsets
    self.offsets = {
        coords = 0,
        texCoords = self.sizes.coords
    }
    if colorSupport then
        self.offsets.color = self.offsets.texCoords + self.sizes.texCoords
    end

    -- vertex data stride (total size of the vertex components)
    self.vertexStride = 0
    for _,size in pairs(self.sizes) do
        self.vertexStride = self.vertexStride + size
    end

    -- vertex data
    self.vertexVbd = ae.gl.VertexBufferData.new(
        self.vertexStride,vertexCapacity)
    self.vertexIndex = 0
    
    -- index data
    self.indexVbd = ae.gl.VertexBufferData.new(
        ae.gl.GLushortSize,indexCapacity)
    self.indexIndex = 0
end

-- @func
-- @brief Appends indices to the buffer.
-- @param indices The indices to append.
-- @func
-- @brief Appends indices to the buffer.
-- @param indices The indices to append.
-- @param shift The value to add to each index.
function TexDrawerBuffer:appendIndices(indices,shift)
    shift = shift or 0
    for _,index in ipairs(indices) do
        self.indexVbd:set1us(self.indexIndex,0,index + shift)
        self.indexIndex = self.indexIndex + 1
    end
end

-- @brief Converts a float to a normalized unsigned short.
-- @param floatValue The float value.
-- @return The unsigned short value.
local function floatToUShort(floatValue)
    return floatValue * 65535
end

-- @brief Converts a float to a normalized unsigned byte.
-- @param floatValue The float value.
-- @return The unsigned byte value.
local function floatToUByte(floatValue)
    return floatValue * 255
end

-- @func
-- @brief Appends a vertex.
-- @param x The vertex X coordinate.
-- @param y The vertex Y coordinate.
-- @param u The texture U coordinate.
-- @param v The texture V coordinate.
-- @param r The red component of the vertex color.
-- @param g The green component of the vertex color.
-- @param b The blue component of the vertex color.
-- @param a The alpha component of the vertex color.
-- @func
-- @brief Appends a vertex.
-- @param x The vertex X coordinate.
-- @param y The vertex Y coordinate.
-- @param u The texture U coordinate.
-- @param v The texture V coordinate.
-- @param color The vertex color.
function TexDrawerBuffer:appendVertex(x,y,u,v,r,g,b,a)
    self.vertexVbd:set2f(self.vertexIndex,self.offsets.coords,x,y)
    self.vertexVbd:set2us(self.vertexIndex,self.offsets.texCoords,
        floatToUShort(u),floatToUShort(v))
    
    -- rgba
    if r and g then
        self.vertexVbd:set4ub(self.vertexIndex,self.offsets.color,
            floatToUByte(r),floatToUByte(g),floatToUByte(b),floatToUByte(a))
    end
        
    -- color
    if r and not g then
        local color = r
        self.vertexVbd:set4ub(self.vertexIndex,self.offsets.color,
            floatToUByte(color.r),floatToUByte(color.g),
            floatToUByte(color.b),floatToUByte(color.a))
    end
    
    self.vertexIndex = self.vertexIndex + 1
end

-- @brief Moves the indexes to the beginning of the buffer.
-- @full Moves the indexes to the beginning of the buffer. The next appended
--   indices/vertices will be start from the buffer beginning.
function TexDrawerBuffer:rewind()
    self.indexIndex = 0
    self.vertexIndex = 0
end

-- @brief Sets the coordinates of a rectangle.
-- @param index The index of the first vertex of the rectangle.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The rectangle width.
-- @param height The rectangle height.
function TexDrawerBuffer:setRectCoords(index,x,y,width,height)
    self.vertexVbd:set2f(index,self.offsets.coords,x,y + height)
    self.vertexVbd:set2f(index + 1,self.offsets.coords,x + width,y + height)
    self.vertexVbd:set2f(index + 2,self.offsets.coords,x + width,y)
    self.vertexVbd:set2f(index + 3,self.offsets.coords,x,y)
end

-- @brief Sets the subtexture coordinate of a rectangle.
-- @full Sets the subtexture coordinate of a rectangle. The next 4 vertices
--   starting with `index` are assigned subtexture coordinates from `uv`.
-- @param index The index of the first vertex of the rectangle.
-- @param uv The UV rectangle of the subtexture.
function TexDrawerBuffer:setRectTexCoords(index,uv)
    self.vertexVbd:set2us(
        index,self.offsets.texCoords,
        floatToUShort(uv.u),floatToUShort(uv.v))
    self.vertexVbd:set2us(
        index + 1,self.offsets.texCoords,
        floatToUShort(uv.u + uv.width),floatToUShort(uv.v))
    self.vertexVbd:set2us(
        index + 2,self.offsets.texCoords,
        floatToUShort(uv.u + uv.width),floatToUShort(uv.v + uv.height))
    self.vertexVbd:set2us(
        index + 3,self.offsets.texCoords,
        floatToUShort(uv.u),floatToUShort(uv.v + uv.height))
end

-- @brief Creates the vertex buffer object for the vertex daa.
-- @full Creates the vertex buffer object for the vertex data. The vertex
--   data is discarded.
function TexDrawerBuffer:createVertexVBO()
    local vertexVbo = VertexBufferObject.new()
    vertexVbo:generate()
    vertexVbo:bind(ae.gl.GL_ARRAY_BUFFER)
    vertexVbo:data(self.vertexVbd,ae.gl.GL_ARRAY_BUFFER,ae.gl.GL_STATIC_DRAW)
    ae.gl.VertexBufferObject.unbind(ae.gl.GL_ARRAY_BUFFER)
    
    self.vertexVbo = vertexVbo
    self.vertexVbd = nil
end

-- @brief Creates the vertex buffer object for the indices.
-- @full Creates the vertex buffer object for the indices. The index data is
--   discarded.
function TexDrawerBuffer:createIndexVBO()
    local indexVbo = VertexBufferObject.new()
    indexVbo:generate()
    indexVbo:bind(ae.gl.GL_ELEMENT_ARRAY_BUFFER)
    indexVbo:data(self.indexVbd,ae.gl.GL_ELEMENT_ARRAY_BUFFER,
        ae.gl.GL_STATIC_DRAW)
    ae.gl.VertexBufferObject.unbind(ae.gl.GL_ELEMENT_ARRAY_BUFFER)
    
    self.indexVbo = indexVbo
    self.indexVbd = nil
end

-- @brief Appends a rectangle to the buffer.
-- @full Appends a rectangle to the buffer. A rectangle takes 6 indices and
--   4 vertices.
-- @param x The X coordinate.
-- @param y The Y coordinate.
-- @param width The rectangle width.
-- @param height The rectangle height.
-- @param uv The UV rectangle within the texture.
-- @param r The red component of the vertex color.
-- @param g The green component of the vertex color.
-- @param b The blue component of the vertex color.
-- @param a The alpha component of the vertex color.
-- @return The index of the first vertex of the rectangle.
function TexDrawerBuffer:appendRect(x,y,width,height,uv,r,g,b,a)
    self:appendIndices({0,1,2, 0,2,3},self.vertexIndex)
    local index = self.vertexIndex
    
    if uv then
        self:appendVertex(x,y + height,
            uv.u,uv.v,
            r,g,b,a)
        self:appendVertex(x + width,y + height,
            uv.u + uv.width,uv.v,
            r,g,b,a)
        self:appendVertex(x + width,y,
            uv.u + uv.width,uv.v + uv.height,
            r,g,b,a)
        self:appendVertex(x,y,
            uv.u,uv.v + uv.height,
            r,g,b,a)
    else
        self:appendVertex(x,y + height,        0,0,r,g,b,a)
        self:appendVertex(x + width,y + height,0,0,r,g,b,a)
        self:appendVertex(x + width,y,         0,0,r,g,b,a)
        self:appendVertex(x,y,                 0,0,r,g,b,a)
    end
    
    return index
end

return TexDrawerBuffer