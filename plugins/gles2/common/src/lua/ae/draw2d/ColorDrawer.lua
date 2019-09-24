-- @group UI
-- @brief The drawer capable of drawing colored triangles.
local ColorDrawer = ae.oo.class()

-- shortcut
local gl = ae.gl

-- The vertex shader template.
local vertShaderTemplate = [[
#ifndef GL_ES
#define lowp
#endif

$ifdef transform
uniform lowp mat4 u_Matrix;
$end
attribute lowp vec2 a_Coords;

void main() {
$ifndef transform
    gl_Position = vec4(a_Coords.x,a_Coords.y,0,1);
$end
$ifdef transform
    gl_Position = u_Matrix * vec4(a_Coords.x,a_Coords.y,0,1);
$end
}
]]

-- The fragment shader template.
local fragShaderTemplate = [[
#ifndef GL_ES
#define lowp
#endif

uniform lowp vec4 u_Color;

void main() {
    gl_FragColor = u_Color;
}
]]

-- @func
-- @brief Creates a color drawer.
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates. Get the matrix via
--   ()[ae.draw2d.ColorDrawer#:getMatrix], update it accordingly and
--   apply the changes via ()[ae.draw2d.ColorDrawer#:updateTransformation].
-- @return The drawer.
-- @func
-- @brief Creates a color drawer.
-- @param transform Indicates if to enable transformation of the supplied\
--   vertex coordinates. Get the matrix via
--   ()[ae.draw2d.ColorDrawer#:getMatrix], update it accordingly and
--   apply the changes via ()[ae.draw2d.ColorDrawer#:updateTransformation].
-- @param buffer The drawer buffer.
-- @return The drawer.
function ColorDrawer.new(transform,buffer)
    local self = ae.oo.new(ColorDrawer)
    ColorDrawer.construct(self,transform,buffer)
    
    return self
end

-- @func
-- @brief Constructs a color drawer.
-- @param self The color drawer object.
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates.
-- @func
-- @brief Constructs a color drawer.
-- @param self The color drawer object.
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates.
-- @param buffer The buffer.
function ColorDrawer.construct(self,transform,buffer)
    self.buffer = buffer
    
    local defines = {}
    if transform then
        table.insert(defines,'transform')
    end    
    
    -- vertex shaders
    local vertShaderSrc = ae.draw2d.shader.preprocess(
        vertShaderTemplate,defines)    
    local vertShader = gl.GLSLShader.compile(gl.GLSLShader.vertex,vertShaderSrc)
    
    -- fragment shader
    local fragShaderSrc = ae.draw2d.shader.preprocess(
        fragShaderTemplate,defines)    
    local fragShader = gl.GLSLShader.compile(
        gl.GLSLShader.fragment,fragShaderSrc)
        
    -- GLSL program
    self.program = gl.GLSLProgram.new()
    self.program:attach(vertShader)
    self.program:attach(fragShader)
    self.program:link()
    self.program:use()
    
    -- GLSL variables
    self.u_Color = gl.GLSLUniform.new(self.program:uniform('u_Color'))
    self.a_Coords = gl.GLSLAttrib.new(self.program:attrib('a_Coords'))
    
    -- transformation variable and matrix
    if transform then
        self.u_Matrix = gl.GLSLUniform.new(self.program:uniform('u_Matrix'))
        self.matrix = Mat4.new()
        self.matrix:identity()
        self:updateTransformation()    
    end
end

-- @func
-- @brief Sets the drawer color.
-- @param color The color with the RGBA components.
-- @func
-- @brief Sets the drawer color.
-- @param r The red component.
-- @param g The green component.
-- @param b The blue component.
-- @param a The alpha component.
function ColorDrawer:setColor(r,g,b,a)
    if g then
        self.u_Color:set4f(r,g,b,a)
    else
        local color = r
        self.u_Color:set4f(color.r,color.g,color.b,color.a)
    end
end

-- @brief Gets the transformation matrix.
-- @return The transformation matrix.
function ColorDrawer:getMatrix()
    return self.matrix
end

-- @brief Updates the shader transformation matrix.
function ColorDrawer:updateTransformation()
    self.u_Matrix:mat4(self.matrix)
end

-- @func
-- @brief Initializes drawing.
-- @func
-- @brief Initializes drawing.
-- @param buffer The buffer whose contants to draw.
function ColorDrawer:drawStart(buffer)
    self.program:use()
    local buffer = buffer or self.buffer
    
    -- vertex coordinates
    self.a_Coords:enable()
    VertexBufferObject.unbind(gl.GL_ARRAY_BUFFER)
    self.a_Coords:pointer(buffer.coordsVbd,2,
        buffer.types.coords,buffer.offsets.coords,false)    
end

-- @func
-- @brief Draws a buffer.
-- @param count The number of elements to render.
-- @func
-- @brief Draws a buffer.
-- @param count The number of elements to render.
-- @param buffer The buffer to draw.
function ColorDrawer:draw(count,buffer)
    local buffer = buffer or self.buffer
    VertexBufferObject.unbind(gl.GL_ELEMENT_ARRAY_BUFFER)
    gl.drawElements(gl.GL_TRIANGLES,count,
        gl.GL_UNSIGNED_SHORT,buffer.indexVbd)
end

return ColorDrawer