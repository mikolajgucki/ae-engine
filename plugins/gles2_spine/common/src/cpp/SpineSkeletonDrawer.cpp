#include "ae_defs.h"
#include "GLColorUtil.h"
#include "GLSLShader.h"
#include "Texture.h"
#include "GLESTexture.h"
#include "luaTexture.h"
#include "SpineSkeletonDrawer.h"

using namespace std;
using namespace ae::math;
using namespace ae::gles2;
using namespace ae::texture;
using namespace ae::texture::lua;

namespace ae {
    
namespace spine {
    
/// The vertex shader source.
static const char *vertShaderSrc = 
    "#ifndef GL_ES\n"
    "#define mediump\n"
    "#endif\n"
    ""
    "uniform mediump mat4 u_Matrix;\n"
    "attribute mediump vec2 a_Coords;\n"
    "attribute mediump vec2 a_TexCoords;\n"
    "varying mediump vec2 v_TexCoords;\n"
    "attribute mediump vec4 a_Color;\n"
    "varying mediump vec4 v_Color;\n"
    ""
    "void main() {\n"
    "    v_Color = a_Color;\n"
    "    v_TexCoords = a_TexCoords;\n"
    "    gl_Position = u_Matrix * vec4(a_Coords.x,a_Coords.y,0,1);\n"
    "}";
    
/// The fragment shader source
static const char *fragShaderSrc =
    "#ifndef GL_ES\n"
    "#define mediump\n"
    "#endif\n"
    ""
    "uniform sampler2D u_Tex;\n"
    "varying mediump vec2 v_TexCoords;\n"
    "uniform mediump vec4 u_Color;\n"
    "varying mediump vec4 v_Color;\n"
    ""
    "void main() {\n"
    "    mediump vec4 texColor = texture2D(u_Tex,v_TexCoords);\n"
    "    gl_FragColor = texColor * u_Color * v_Color;\n"
    "}";
    
/// The indices of a quad built from two triangles.
static const int quadTriangles[6] = {0, 1, 2, 2, 3, 0};
    
/** */
void SpineSkeletonDrawer::create() {
    const int vbdCapacity = MAX_COORDS;
    
// coordinates vertex buffer data
    coordsStride = 
        sizeof(GLfloat) * 4 + // x,y + u,v
        sizeof(GLubyte) * 4; // r,g,b,a
    coordsVbd = new VertexBufferData(coordsStride,vbdCapacity);
    if (coordsVbd->allocate() == false) {
        setError(coordsVbd->getError());
        return;
    }
    
// index vertex buffer data
    const int indexStride = sizeof(GLushort);
    indexVbd = new VertexBufferData(indexStride,vbdCapacity);    
    if (indexVbd->allocate() == false) {
        setError(indexVbd->getError());
        return;
    }
    
// vertex shader
    GLSLShader vertShader(GL_VERTEX_SHADER);
    if (vertShader.create() == false) {
        setError(vertShader.getError());
        return;
    }
    if (vertShader.compile(vertShaderSrc) == false) {
        setError(vertShader.getError());
        return;
    }   

// fragment shader
    GLSLShader fragShader(GL_FRAGMENT_SHADER);
    if (fragShader.create() == false) {
        setError(fragShader.getError());
        return;
    }
    if (fragShader.compile(fragShaderSrc) == false) {
        setError(fragShader.getError());
        return;
    }
    
// program
    if (program.create() == false) {
        setError(program.getError());
        return;
    }
    if (program.attachShader(&vertShader) == false) {
        setError(program.getError());
        return;
    }
    if (program.attachShader(&fragShader) == false) {
        setError(program.getError());
        return;
    }
    if (program.link() == false) {
        setError(program.getError());
        return;
    }
    
// uniforms
    program.getUniformLocation("u_Color",&u_Color);
    program.getUniformLocation("u_Tex",&u_Tex);
    program.getUniformLocation("u_Matrix",&u_Matrix);
    
// attributes
    program.getAttribLocation("a_Coords",&a_Coords);
    program.getAttribLocation("a_TexCoords",&a_TexCoords);
    program.getAttribLocation("a_Color",&a_Color);
    
    chkGLError();
}
    
/** */
SpineSkeletonDrawer::~SpineSkeletonDrawer() {
    if (coordsVbd != (VertexBufferData *)0) {
        delete coordsVbd;
    }
    if (indexVbd != (VertexBufferData *)0) {
        delete indexVbd;
    }
}

/** */
void SpineSkeletonDrawer::setBlending(spSlot *slot) {
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
}

/** */
bool SpineSkeletonDrawer::draw(SpineAttachmentRendererObject *rendererObject,
    float *coords,float *uvs,const int *indices,int vertexCount,
    int indexCount) {
// cast to GLES texture
#ifndef AE_ANDROID
    GLESTexture *glesTexture =
        dynamic_cast<GLESTexture *>(rendererObject->textureType->texture);
    if (glesTexture == (GLESTexture *)0) {
        setError("Not a GLES texture");
        return false;
    }        
#else
    GLESTexture *glesTexture =
        (GLESTexture *)(rendererObject->textureType->texture);
#endif        
        
// bind texture
    if (glesTexture->bind() == false) {
        setError(rendererObject->textureType->texture->getError());
        return false;
    }    
       
    GLubyte r = (GLubyte)rendererObject->color.getRed();
    GLubyte g = (GLubyte)rendererObject->color.getGreen();
    GLubyte b = (GLubyte)rendererObject->color.getBlue();
    GLubyte a = (GLubyte)rendererObject->color.getAlpha();

// coordinates, color, texture coordinates
    for (int index = 0; index < vertexCount; index++) {
        coordsVbd->set2f(index,0,
            (GLfloat)coords[index * 2],(GLfloat)coords[index * 2 + 1]);
        coordsVbd->set2f(index,sizeof(GLfloat) * 2,
            (GLfloat)uvs[index * 2],(GLfloat)uvs[index * 2 + 1]);
        coordsVbd->set4ub(index,sizeof(GLfloat) * 4,r,g,b,a);
    }

// coordinates attribute
    glEnableVertexAttribArray(a_Coords);
    glVertexAttribPointer(a_Coords,2,GL_FLOAT,GL_FALSE,coordsStride,
        coordsVbd->getPointer());
    if (chkGLError()) {
        return false;
    }    
    
// texture coordinates attribute
    glEnableVertexAttribArray(a_TexCoords);
    glVertexAttribPointer(a_TexCoords,2,GL_FLOAT,GL_FALSE,coordsStride,
        coordsVbd->getPointer(sizeof(GLfloat) * 2));
    if (chkGLError()) {
        return false;
    }
    
// color attribute
    glEnableVertexAttribArray(a_Color);
    glVertexAttribPointer(a_Color,4,GL_UNSIGNED_BYTE,GL_TRUE,coordsStride,
        coordsVbd->getPointer(sizeof(GLfloat) * 4));
    if (chkGLError()) {
        return false;
    }
    
// indices
    for (int index = 0; index < indexCount; index++) {
        indexVbd->set1us(index,0,(GLushort)indices[index]);
    }
    
// draw
    glDrawElements(GL_TRIANGLES,indexCount,GL_UNSIGNED_SHORT,
        indexVbd->getPointer());
    if (chkGLError()) {
        return false;
    }   
    
    return true;
}

/** */
bool SpineSkeletonDrawer::drawRegion(spSlot *slot,spRegionAttachment *region) {
    SpineAttachmentRendererObject *object =
        (SpineAttachmentRendererObject *)region->rendererObject;
    
    spRegionAttachment_computeWorldVertices(region,slot->bone,coords);
    setBlending(slot);
    return draw(object,coords,region->uvs,quadTriangles,8,6);
}

/** */
bool SpineSkeletonDrawer::drawMesh(spSlot *slot,spMeshAttachment *mesh) {
    SpineAttachmentRendererObject *object =
        (SpineAttachmentRendererObject *)mesh->rendererObject;
        
    spMeshAttachment_computeWorldVertices(mesh,slot,coords);
    setBlending(slot);
    return draw(object,coords,mesh->uvs,mesh->triangles,
        mesh->verticesCount,mesh->trianglesCount);
}

/** */
bool SpineSkeletonDrawer::drawSkinnedMesh(spSlot *slot,
    spSkinnedMeshAttachment *skinnedMesh) {
//
    SpineAttachmentRendererObject *object =
        (SpineAttachmentRendererObject *)skinnedMesh->rendererObject;
        
    spSkinnedMeshAttachment_computeWorldVertices(skinnedMesh,slot,coords);
    setBlending(slot);
    return draw(object,coords,skinnedMesh->uvs,
        skinnedMesh->triangles,skinnedMesh->uvsCount,
        skinnedMesh->trianglesCount);        
}

/** */
bool SpineSkeletonDrawer::drawStart(const Color &color) {
// use the program
    program.use();
    if (chkGLError()) {
        return false;
    }
    
// unbind buffers
    glBindBuffer(GL_ARRAY_BUFFER,0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    if (chkGLError()) {
        return false;
    }    
    
// blending
    glEnable(GL_BLEND);
    if (chkGLError()) {
        return false;
    }
    
// color
    GLColorUtil::uniform(u_Color,color);
    if (chkGLError()) {
        return false;
    }    
    
// texture
    glActiveTexture(GL_TEXTURE0);
    if (chkGLError()) {
        return false;
    }
    glUniform1i(u_Tex,0);
    if (chkGLError()) {
        return false;
    } 

    return true;
}

/** */
bool SpineSkeletonDrawer::draw(spSkeleton *skeleton,
    const Mat4 &transformation) {
// transformation
    glUniformMatrix4fv(u_Matrix,1,GL_FALSE,transformation.asArray());    
    if (chkGLError()) {
        return false;
    }

// for each slot
    for (int slotIndex = 0; slotIndex < skeleton->slotsCount; slotIndex++) {
        spSlot* slot = skeleton->drawOrder[slotIndex];
        if (!slot->attachment) {
            continue;
        }        
        
        switch (slot->attachment->type) {
            case SP_ATTACHMENT_REGION:
                if (drawRegion(slot,
                    (spRegionAttachment *)slot->attachment) == false) {
                //
                    return false;
                }
                break;
                
            case SP_ATTACHMENT_MESH:
                if (drawMesh(slot,
                    (spMeshAttachment *)slot->attachment) == false) {
                //
                    return false;
                }
                break;
                
            case SP_ATTACHMENT_SKINNED_MESH:
                if (drawSkinnedMesh(slot,
                    (spSkinnedMeshAttachment *)slot->attachment) == false) {
                //
                    return false;
                }
                break;
                
            default:
                break;
        }
    }
    
    return true;
}
    
/** */
bool SpineSkeletonDrawer::drawEnd() {
// default blend function
    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    
    return true;
}

} // namespace
    
} // namespace