// http://www.songho.ca/opengl/gl_vbo.html
#include <memory>

#include "ae_defs.h"
#include "Log.h"
#include "GLTexture.h"
#include "GLGPUQueue.h"
#include "GLDefaultDrawer.h"

/// Gets the pointer from either VBO or VBD
#define GET_POINTER(offset) vbo != NO_VBO ? (void *)offset : vbd->getPointer(offset)

using namespace std;
using namespace ae::math;
using namespace ae::texture;
using namespace ae::gpu;

namespace ae {

namespace gles {

/** */
class DrawArraysCommand:public GLGPUQueueCommand {
    /// The drawer.
    GLDefaultDrawer *drawer;
    
    /// The position of the first vertex to draw.
    int first;
    
    /// The number of vertices to draw.
    int count;
    
    /// The drawer index.
    GLDrawerIndex *drawerIndex;
    
    /// The texture.
    GLTexture *texture;
    
    /// The transformation matrix.
    const Mat4 *transformation;
    
public:
    /** */
    DrawArraysCommand(GLDefaultDrawer *drawer_,int first_,int count_,
        GLDrawerIndex *drawerIndex_,GLTexture *texture_,
        const Mat4 *transformation_):GLGPUQueueCommand(),drawer(drawer_),
        first(first_),count(count_),drawerIndex(drawerIndex_),
        texture(texture_),transformation(transformation_) {
    }
    
    /** */
    virtual ~DrawArraysCommand() {
    }
    
    /** */
    virtual bool execute() {
        drawer->draw(first,count,drawerIndex,texture,transformation);
        return true;
    }
};

/** */
GLDefaultDrawer::GLDefaultDrawer(DefaultDrawerFeatures features,int capacity,
    GLSLProgram *glslProgram_):DefaultDrawer(features,capacity),glError(),
    glslProgram(glslProgram_),vbd((VertexBufferData *)0),
    subdata((VertexBufferData *)0),vertexCoordsSubdata((GLfloat *)0),
    vertexColorSubdata((GLubyte *)0),textureCoordsSubdata((GLushort *)0),
    vertexCoordsSize(NO_VALUE),vertexColorSize(NO_VALUE),
    textureCoordsSize(NO_VALUE),stride(0),vertexColorOffset(NO_VALUE),
    textureCoordsOffset(NO_VALUE),a_Coords(-1),u_Color(-1),a_Color(-1),
    a_TexCoords(-1),u_Tex(-1),vbo(NO_VBO) {
// 
    if (createVBD() == false) {
        return;
    }
    if (createSubdata() == false) {
        return;
    }
    getGLSLVariables();
}

/** */
GLDefaultDrawer::~GLDefaultDrawer() {
    if (vbo != NO_VBO) {
        glDeleteBuffers(1,&vbo);
    }
    if (vbd != (VertexBufferData *)0) {
        delete vbd;
    }
}

/** */
bool GLDefaultDrawer::chkGLError() {
    if (glError.chkGLError() == false) {
        setError(glError.getError());
        return false;
    }
    
    return false;
}

/** */
bool GLDefaultDrawer::createVBD() {
    int offset = 0;
    
// vertex coordinates
    vertexCoordsSize = sizeof(GLfloat) * 2; // x,y
    offset += vertexCoordsSize;
    
// vertex color
    if (getFeatures().hasVertexColor() == true) {
        vertexColorSize = sizeof(GLubyte) * 4; // rgba
        vertexColorOffset = offset;
        offset += vertexColorSize;
    }
    
// texture
    if (getFeatures().hasTexture() == true) {
        textureCoordsSize = sizeof(GLushort) * 2; // normalized u,v
        textureCoordsOffset = offset;
        offset += textureCoordsSize;
    }
    
// stride
    stride = offset;
    
// create VBD
    vbd = new (nothrow) VertexBufferData(stride,getCapacity());
    if (vbd == (VertexBufferData *)0) {
        setNoMemoryError();
        return false;
    }
    if (vbd->allocate() == false) {
        setError(vbd->getError());
        delete vbd;
        return false;
    }
    
    return true;
}

/** */
bool GLDefaultDrawer::createSubdata() {
// create
    subdata = new (nothrow) VertexBufferData(stride,1);
    if (subdata == (VertexBufferData *)0) {
        setNoMemoryError();
        return false;
    }
    if (subdata->allocate() == false) {
        setError(subdata->getError());
        delete subdata;
        return false;
    }    
    
// pointers
    vertexCoordsSubdata = subdata->getGLfloatData(0,0);
    vertexColorSubdata = subdata->getGLubyteData(0,vertexColorOffset);
    textureCoordsSubdata = subdata->getGLushortData(0,textureCoordsOffset);
    
    return true;
}

/** */
void GLDefaultDrawer::getGLSLVariables() {
    glslProgram->use();
    
// vertex coordinates
    if (glslProgram->getAttribLocation("a_Coords",&a_Coords) == false) {
        setError(glslProgram->getError());
        return;
    }

// transformation
    if (glslProgram->getUniformLocation(
        "u_Transformation",&u_Transformation) == false) {
    //
        setError(glslProgram->getError());
        return;
    }
    
// color
    if (getFeatures().hasColor() == true) {
        if (glslProgram->getUniformLocation("u_Color",&u_Color) == false) {
            setError(glslProgram->getError());
            return;
        }
    }
    
// vertex color
    if (getFeatures().hasVertexColor() == true) {
        if (glslProgram->getAttribLocation("a_Color",&a_Color) == false) {
            setError(glslProgram->getError());
            return;
        }
    }
    
// texture
    if (getFeatures().hasTexture() == true) {
        if (glslProgram->getUniformLocation("u_Tex",&u_Tex) == false) {
            setError(glslProgram->getError());
            return;
        }
        if (glslProgram->getAttribLocation(
            "a_TexCoords",&a_TexCoords) == false) {
        //
            setError(glslProgram->getError());
            return;
        }
    }
}

/** */    
bool GLDefaultDrawer::activate() {
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
    }
    
    return true;
}

/** */    
bool GLDefaultDrawer::setVertexCoords(int position,float x,float y) {
    if (checkVertexPosition(position) == false) {
        return false;
    }
    
// VBD
    if (vbd != (VertexBufferData *)0) {
        vbd->set2f(position,0,(GLfloat)x,(GLfloat)y);
    }
    
// VBO
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        
        vertexCoordsSubdata[0] = (GLfloat)x;
        vertexCoordsSubdata[1] = (GLfloat)y;
        glBufferSubData(GL_ARRAY_BUFFER,getOffset(position,0),
            vertexCoordsSize,(void *)vertexCoordsSubdata);        
    }
    
    return true;    
}    
    
/** */
bool GLDefaultDrawer::setVertexColor(int position,Color color) {
    if (checkVertexPosition(position) == false) {
        return false;
    }
#ifdef AE_DEBUG
    if (vertexColorOffset == NO_VALUE) {
        setError("Vertex color feature not enabled");
        return false;
    }
#endif
    
// VBD
    if (vbd != (VertexBufferData *)0) {
        vbd->set4ub(position,vertexColorOffset,
            (GLubyte)(255 * color.getRed()),
            (GLubyte)(255 * color.getGreen()),
            (GLubyte)(255 * color.getBlue()),
            (GLubyte)(255 * color.getAlpha()));
    }
    
// VBO
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        
        vertexColorSubdata[0] = (GLubyte)(255 * color.getRed());
        vertexColorSubdata[1] = (GLubyte)(255 * color.getGreen());
        vertexColorSubdata[2] = (GLubyte)(255 * color.getBlue());
        vertexColorSubdata[3] = (GLubyte)(255 * color.getAlpha());
        glBufferSubData(GL_ARRAY_BUFFER,getOffset(position,vertexColorOffset),
            vertexColorSize,(void *)vertexColorSubdata);
    }
    
    return true;
}

/** */    
bool GLDefaultDrawer::setTextureCoords(int position,float u,float v) {
    if (checkVertexPosition(position) == false) {
        return false;
    }
#ifdef AE_DEBUG
    if (textureCoordsOffset == NO_VALUE) {
        setError("Texture feature not enabled");
        return false;
    }
#endif
    
// VBD
    if (vbd != (VertexBufferData *)0) {
        vbd->set2us(position,textureCoordsOffset,
            GL_TO_NORMALIZED_USHORT(u),GL_TO_NORMALIZED_USHORT(v));
    }
    
// VBO
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        
        textureCoordsSubdata[0] = GL_TO_NORMALIZED_USHORT(u);
        textureCoordsSubdata[1] = GL_TO_NORMALIZED_USHORT(v);
        glBufferSubData(GL_ARRAY_BUFFER,
            getOffset(position,textureCoordsOffset),
            textureCoordsSize,(void *)textureCoordsSubdata);
    }
    
    return true;    
}

/** */
bool GLDefaultDrawer::setCoords(int position,float x,float y,float u,float v) {
    if (checkVertexPosition(position) == false) {
        return false;
    }
#ifdef AE_DEBUG
    if (textureCoordsOffset == NO_VALUE) {
        setError("Texture feature not enabled");
        return false;
    }
#endif
    
// VBD
    if (vbd != (VertexBufferData *)0) {
        vbd->set2f(position,0,(GLfloat)x,(GLfloat)y);
        vbd->set2us(position,textureCoordsOffset,
            GL_TO_NORMALIZED_USHORT(u),GL_TO_NORMALIZED_USHORT(v));
    }    
    
// VBO
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        
        vertexCoordsSubdata[0] = (GLfloat)x;
        vertexCoordsSubdata[1] = (GLfloat)y;
        glBufferSubData(GL_ARRAY_BUFFER,getOffset(position,0),
            vertexCoordsSize,(void *)vertexCoordsSubdata);     
        
        textureCoordsSubdata[0] = GL_TO_NORMALIZED_USHORT(u);
        textureCoordsSubdata[1] = GL_TO_NORMALIZED_USHORT(v);
        glBufferSubData(GL_ARRAY_BUFFER,getOffset(position,textureCoordsOffset),
            textureCoordsSize,(void *)textureCoordsSubdata);        
    }
    
    return true;
}

/** */
bool GLDefaultDrawer::deleteCPUData() {
#ifdef AE_DEBUG
    if (vbd == (VertexBufferData *)0) {
        setError("CPU data already deleted");
        return false;
    }
    if (vbo == NO_VBO) {
        setError("Cannot delete CPU data before moving to GPU");
        return false;
    }
#endif
    
// delete
    delete vbd;
    vbd = (VertexBufferData *)0;
    
    return true;
}

/** */
bool GLDefaultDrawer::moveToGPU() {
#ifdef AE_DEBUG
    if (vbo != NO_VBO) {
        setError("Already moved to GPU");
        return false;
    }
#endif
    
// create
    glGenBuffers(1,&vbo);
    if (chkGLError()) {
        return false;
    }
    
    /*
    ostringstream m0;
    m0 << "vbo=" << vbo << "\n";
    fprintf(stderr,"%s",m0.str().c_str());
    fflush(stderr);
    */
    
// bind
    glBindBuffer(GL_ARRAY_BUFFER,vbo);
    if (chkGLError()) {
        return false;
    }
    
// set data
    glBufferData(GL_ARRAY_BUFFER,vbd->getSize(),vbd->getPointer(),
        GL_DYNAMIC_DRAW);
    if (chkGLError()) {
        return false;
    }
    
    return true;
}

/** */
bool GLDefaultDrawer::enqueue(GPUQueue *queue,int first,int count,
    DrawerIndex *drawerIndex,Texture *texture,const Mat4 *transformation) {
// cast queue
#ifndef AE_ANDROID
    GLGPUQueue *glQueue = dynamic_cast<GLGPUQueue *>(queue);
#ifdef AE_DEBUG
    if (glQueue == (GPUQueue *)0) {
        setError("Not a GPU GLES queue");
        return false;
    }
#endif // AE_DEBUG
#else
    GLGPUQueue *glQueue = (GLGPUQueue *)queue;
#endif // AE_ANDROID

// cast drawer index
    GLDrawerIndex *glDrawerIndex = (GLDrawerIndex *)0;
    if (drawerIndex != (DrawerIndex *)0) {
#ifndef AE_ANDROID
        glDrawerIndex = dynamic_cast<GLDrawerIndex *>(drawerIndex);
#ifdef AE_DEBUG
        if (glDrawerIndex == (GLDrawerIndex *)0) {
            setError("Not a GPU GLES drawer index");
            return false;        
        }
#endif // AE_DEBUG
#else
        glDrawerIndex = (GLDrawerIndex *)drawerIndex;
#endif // AE_ANDROID
    }
    
// cast texture
    GLTexture *glTexture = (GLTexture *)0;
#ifdef AE_DEBUG
    if (getFeatures().hasTexture() == true &&
        texture == (Texture *)0) {
    //
        setError("No texture passed w/texture feature enabled");
        return false;
    }
#endif

    if (texture != (Texture *)0) {
#ifndef AE_ANDROID
        glTexture = dynamic_cast<GLTexture *>(texture);
#ifdef AE_DEBUG
        if (glTexture == (GLTexture *)0) {
            setError("Not a GL texture");
            return false;
        }
#endif // AE_DEBUG
#else
        glTexture = (GLTexture *)texture;
#endif // AE_ANDROID
    }
    
// create command
    DrawArraysCommand *command = new (nothrow) DrawArraysCommand(
        this,first,count,glDrawerIndex,glTexture,transformation);
    if (command == (DrawArraysCommand *)0) {
        setNoMemoryError();
        return false;
    }
    
// add to the queue
    glQueue->add(command);
    
    return true;
}

/** */
void GLDefaultDrawer::draw(int first,int count,GLDrawerIndex *drawerIndex,
    GLTexture *texture,const Mat4 *transformation) {
// GLSL program
    glslProgram->use();
    
// get the data from VBO
    if (vbo != NO_VBO) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
    }
    else {
    // get the data from VBD
        glBindBuffer(GL_ARRAY_BUFFER,0);
    }    
    
// transformation
    if (transformation == (Mat4 *)0) {
        transformation = &MAT4_IDENTITY;
    }
    glUniformMatrix4fv(u_Transformation,1,GL_FALSE,transformation->asArray());
    
// vertex coordinates
    glEnableVertexAttribArray(a_Coords);
    glVertexAttribPointer(a_Coords,
        2,GL_FLOAT, // x,y
        GL_FALSE, // fixed-point values
        stride,GET_POINTER(0));
    
// color
    if (getFeatures().hasColor() == true) {
        glUniform4f(u_Color,
            getColor().getRed(),
            getColor().getGreen(),
            getColor().getBlue(),
            getColor().getAlpha());
    }
    
// vertex color
    if (getFeatures().hasVertexColor() == true) {
        glEnableVertexAttribArray(a_Color);
        glVertexAttribPointer(a_Color,
            4,GL_UNSIGNED_BYTE, // rgba
            GL_TRUE, // normalized
            stride,GET_POINTER(vertexColorOffset));
    }
    
// texture
    if (getFeatures().hasTexture() == true) {
    // texture coordinates
        glEnableVertexAttribArray(a_TexCoords);
        glVertexAttribPointer(a_TexCoords,
            2,GL_UNSIGNED_SHORT, // u,v
            GL_TRUE, // normalized
            stride,GET_POINTER(textureCoordsOffset));
        
    // texture
        glBindTexture(GL_TEXTURE_2D,texture->getGLName());
        glUniform1i(u_Tex,0);        
    }
    
// no index on the GPU side
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    
// draw
    if (drawerIndex != (GLDrawerIndex *)0) {
        glDrawElements(GL_TRIANGLES,count,GL_UNSIGNED_SHORT,
            drawerIndex->getPointer(first));
    }
    else {
        glDrawArrays(GL_TRIANGLES,first,count);
    }
    
    glFlush();
    
    glDisableVertexAttribArray(a_Coords);
    if (getFeatures().hasVertexColor() == true) {
        glDisableVertexAttribArray(a_Color);
    }
    if (getFeatures().hasTexture() == true) {
        glDisableVertexAttribArray(a_TexCoords);
    }
}
    
} // namespace

} // namespace