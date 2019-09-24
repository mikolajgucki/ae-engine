-- @group UI
-- @brief The drawer capable of drawing textured and colored triangles.
local TexDrawer = ae.oo.class()

-- shortcut
local gl = ae.gl

-- The template for the vertex shader source.
local vertShaderTemplate = [[
#ifndef GL_ES
#define lowp
#endif

$ifdef transform
uniform lowp mat4 u_Matrix;
$end
attribute lowp vec2 a_Coords;
attribute lowp vec2 a_TexCoords;
varying lowp vec2 v_TexCoords;

$ifdef a_Color
attribute lowp vec4 a_Color;
varying lowp vec4 v_Color;
$end

void main() {
    v_TexCoords = a_TexCoords;
$ifdef a_Color
    v_Color = a_Color;
$end    
$ifndef transform
    gl_Position = vec4(a_Coords.x,a_Coords.y,0,1);
$end
$ifdef transform
    gl_Position = u_Matrix * vec4(a_Coords.x,a_Coords.y,0,1);
$end
}
]]

-- The template for the fragment shader source.
local fragShaderTemplate = [[
#ifndef GL_ES
#define lowp
#endif

$ifdef u_Color
uniform lowp vec4 u_Color;
$end
uniform sampler2D u_Tex;
varying lowp vec2 v_TexCoords;

$ifdef a_Color
varying lowp vec4 v_Color;
$end

void main() {
    lowp vec4 texColor = texture2D(u_Tex,v_TexCoords);
$ifdef no_Color
    gl_FragColor = texColor;
$end
$ifdef u_Color
    gl_FragColor = texColor * u_Color;
$end
$ifdef a_Color
    gl_FragColor = texColor * v_Color;
$end
}
]]

-- @func
-- @brief Creates a texture drawer.
-- @param color Indicates if to enable color. This is the color by
--   which the result color is multiplied. Set the color via
--   ()[#ae.draw2d.TexDrawer#:setColor].
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates. Get the matrix via
--   ()[ae.draw2d.TexDrawer#:getMatrix], update it accordingly and
--   apply the changes via ()[ae.draw2d.TexDrawer#:updateTransformation].
-- @return The drawer.
-- @func
-- @brief Creates a texture drawer.
-- @param color Indicates if to enable color. This is the color by
--   which the result color is multiplied. Set the color via
--   ()[ae.draw2d.TexDrawer#:setColor].
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates. Get the matrix via
--   ()[ae.draw2d.TexDrawer#:getMatrix], update it accordingly and
--   apply the changes via ()[ae.draw2d.TexDrawer#:updateTransformation].
-- @param perVertexColor Indicates if the support per vertex color. The vertex
--   colors must be given in the drawer buffer.
-- @param buffer The drawer buffer.
-- @return The drawer.
function TexDrawer.new(color,transform,perVertexColor,buffer)
    local self = ae.oo.new(TexDrawer)
    TexDrawer.construct(self,color,transform,perVertexColor,buffer)
    
    return self
end

-- @func
-- @brief Constructs a drawer.
-- @param self The drawer object.
-- @param color Indicates if to enable color.
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates.
-- @param perVertexColor Indicates if the suppor per vertex color. The vertex
--   colors must be given in the drawe buffer.
-- @func
-- @brief Constructs a drawer.
-- @param self The drawer object.
-- @param color Indicates if to enable color.
-- @param transform Indicates if to enable transformation of the supplied
--   vertex coordinates.
-- @param perVertexColor Indicates if the suppor per vertex color. The vertex
--   colors must be given in the drawe buffer.
-- @param buffer The drawer buffer.
function TexDrawer.construct(self,color,transform,perVertexColor,buffer)
    self.buffer = buffer
    self.perVertexColor = perVertexColor
    
    local defines = {}    
    -- transform define
    if transform then
        table.insert(defines,'transform')
    end
    
    -- color defines
    if color and not self.perVertexColor then
        table.insert(defines,'u_Color')
    end
    if not color and self.perVertexColor then
        table.insert(defines,'a_Color')
    end
    if not color and not self.perVertexColor then
        table.insert(defines,'no_Color')
    end
    
    -- vertex shader
    local vertShaderSrc = ae.draw2d.shader.preprocess(
        vertShaderTemplate,defines)
    local vertShader = gl.GLSLShader.compile(
        gl.GLSLShader.vertex,vertShaderSrc)
    
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
    if color then
        self.u_Color = gl.GLSLUniform.new(self.program:uniform('u_Color'))
        self:setColor(1,1,1,1)
    end
    if self.perVertexColor then
        self.a_Color = gl.GLSLAttrib.new(self.program:attrib('a_Color'))
    end
    if transform then
        self.u_Matrix = gl.GLSLUniform.new(self.program:uniform('u_Matrix'))
        self.matrix = Mat4.new()
        self.matrix:identity()
        self:updateTransformation()
    end
    
    -- coordinates attributes
    self.a_Coords = gl.GLSLAttrib.new(self.program:attrib('a_Coords'))
    self.a_TexCoords = gl.GLSLAttrib.new(self.program:attrib('a_TexCoords'))
       
    -- texture uniform
    self.u_Tex = gl.GLSLUniform.new(self.program:uniform('u_Tex'))    
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
function TexDrawer:setColor(r,g,b,a)
    if g then
        self.u_Color:set4f(r,g,b,a)
    else
        local color = r
        self.u_Color:set4f(color.r,color.g,color.b,color.a)
    end
end

-- @brief Gets the transformation matrix.
-- @return The transformation matrix.

function TexDrawer:getMatrix()
    return self.matrix
end

-- @brief Updates the shader transformation matrix.
function TexDrawer:updateTransformation()
    self.u_Matrix:mat4(self.matrix)
end

-- @func
-- @brief Initializes drawing.
-- @func
-- @brief Initializes drawing.
-- @param buffer The buffer whose contants to draw.
function TexDrawer:drawStart(buffer)
    self.program:use()
    self.u_Tex:set1i(0)
    local buffer = buffer or self.buffer

    -- vertex variables
    self.a_Coords:enable()
    self.a_TexCoords:enable()
    if self.perVertexColor then
        self.a_Color:enable()
    end
    
    -- vertex data
    if buffer.vertexVbd then
        VertexBufferObject.unbind(gl.GL_ARRAY_BUFFER)
        self.a_Coords:pointer(buffer.vertexVbd,2,
            buffer.types.coords,buffer.offsets.coords,false)
        self.a_TexCoords:pointer(buffer.vertexVbd,2,
            buffer.types.texCoords,buffer.offsets.texCoords,true)
        if self.perVertexColor then
            self.a_Color:pointer(buffer.vertexVbd,4,
                buffer.types.color,buffer.offsets.color,true)
        end
    else
        buffer.vertexVbo:bind(gl.GL_ARRAY_BUFFER)
        self.a_Coords:pointer(buffer.vertexStride,2,
            buffer.types.coords,buffer.offsets.coords,false)
        self.a_TexCoords:pointer(buffer.vertexStride,2,
            buffer.types.texCoords,buffer.offsets.texCoords,true)
        if self.perVertexColor then
            self.a_Color:pointer(buffer.vertexStride,4,
                buffer.types.color,buffer.offsets.color,true)
        end
    end
end

-- @func
-- @brief Draws a buffer.
-- @param count The number of elements to render.
-- @func
-- @brief Draws a buffer.
-- @param count The number of elements to render.
-- @param buffer The buffer to draw.
function TexDrawer:draw(count,buffer)
    local buffer = buffer or self.buffer
    if buffer.indexVbd then
        VertexBufferObject.unbind(gl.GL_ELEMENT_ARRAY_BUFFER)
        gl.drawElements(gl.GL_TRIANGLES,count,
            gl.GL_UNSIGNED_SHORT,buffer.indexVbd)
    else
        buffer.indexVbo:bind(gl.GL_ELEMENT_ARRAY_BUFFER)  
        gl.drawElements(gl.GL_TRIANGLES,count,gl.GL_UNSIGNED_SHORT,nil)
    end
end

return TexDrawer
