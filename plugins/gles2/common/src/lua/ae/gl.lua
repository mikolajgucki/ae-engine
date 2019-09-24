-- @group GLES2

-- keep the C library
local _gl = gl

-- Lua library
local gl = _gl

-- C libraries
gl.GLSLShader = GLSLShader
gl.GLSLProgram = GLSLProgram
gl.GLSLAttrib = GLSLAttrib
gl.GLSLUniform = GLSLUniform
gl.VertexBufferData = VertexBufferData
gl.VertexBufferObject = VertexBufferObject

GLSLShader = nil
GLSLProgram = nil
GLSLAttrib = nil
GLSLUniform = nil
GLSLVertexBufferData = nil
GLSLVertexBufferObject = nil

-- @brief Converts display coordinates to GL coordinates.
-- @param x The display X coordinate.
-- @param y The display Y coordinate.
-- @return The GL coordinates `{ x = ..., y = ... }`.
function gl.getGLCoords(x,y)
    return {
        x = (x / ae.display.width) * 2 - 1,
        y = 1 - (y / ae.display.height) * 2
    }
end

-- @brief The GL coordinates of the display.
gl.bounds = {
-- @name gl.bounds.x
-- @var
-- @brief The X coordinate of the lower-left corner of the display.
    x = -1,
    
-- @name gl.bounds.y
-- @var
-- @brief The Y coordinate of the lower-left corner of the display.
    y = -1,
    
-- @name gl.bounds.width
-- @var
-- @brief The width of the display.
    width = 2,
    
-- @name gl.bounds.height
-- @var
-- @brief The height of the display.
    height = 2
}

return gl