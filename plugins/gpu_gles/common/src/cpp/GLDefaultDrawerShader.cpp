#include <memory>

#include "string_util.h"
#include "GLSLShader.h"
#include "GLDefaultDrawerShader.h"

using namespace std;
using namespace ae::util;
using namespace ae::gpu;

namespace ae {
    
namespace gles {
    
/** */
static const char *vertShaderSrc[] = {
    "#ifndef GL_ES",
    "#define lowp",
    "#endif",
    
    "attribute lowp vec2 a_Coords;",
    "$if a_Color",
    "attribute lowp vec4 a_Color;",
    "varying lowp vec4 v_Color;",
    "$end",
    "$if t_Color",
    "attribute lowp vec2 a_TexCoords;",
    "varying lowp vec2 v_TexCoords;",
    "$end",
    "uniform lowp mat4 u_Transformation;",
    
    "void main()",
    "{",
    "$if a_Color",
    "    v_Color = a_Color;",
    "$end",
    "$if t_Color",
    "    v_TexCoords = a_TexCoords;",
    "$end",
    "    gl_Position = u_Transformation * vec4(a_Coords.x,a_Coords.y,0,1);",
    "}",
    (const char *)0
};
    
/** */
static const char *fragShaderSrc[] = {
    "#ifndef GL_ES",
    "#define lowp",
    "#endif",
    
    "$if u_Color",
    "uniform lowp vec4 u_Color;",
    "$end",
    "$if a_Color",
    "varying lowp vec4 v_Color;",
    "$end",
    "$if t_Color",
    "uniform sampler2D u_Tex;",
    "varying lowp vec2 v_TexCoords;",
    "$end",
    
    "void main()",
    "{",
    
    "$if t_Color",
    "    lowp vec4 t_Color = texture2D(u_Tex,v_TexCoords);",
    "$end",
    
    "$if u_Color", // u_Color
    "$ifnot a_Color",
    "$ifnot t_Color",
    "    gl_FragColor = u_Color;",
    "$end",
    "$end",
    "$end",
    
    "$if a_Color", // a_Color
    "$ifnot u_Color",
    "$ifnot t_Color",
    "    gl_FragColor = v_Color;",
    "$end",
    "$end",
    "$end",
    
    "$if t_Color", // t_Color
    "$ifnot u_Color",
    "$ifnot a_Color",
    "    gl_FragColor = t_Color;",
    "$end",
    "$end",
    "$end",
    
    "$if u_Color", // u_Color + a_Color
    "$if a_Color",
    "$ifnot t_Color",
    "    gl_FragColor = u_Color * v_Color;",
    "$end",
    "$end",
    "$end",
    
    "$if u_Color", // u_Color + t_Color
    "$if t_Color",
    "$ifnot a_Color",
    "    gl_FragColor = u_Color * t_Color;",
    "$end",
    "$end",
    "$end",

    "$if a_Color", // a_Color + t_Color
    "$if t_Color",
    "$ifnot u_Color",
    "    gl_FragColor = v_Color * t_Color;",
    "$end",
    "$end",
    "$end",

    "$if a_Color", // a_Color + u_Color + t_Color
    "$if u_Color",
    "$if t_Color",
    "    gl_FragColor = u_Color * v_Color * t_Color;",
    "$end",
    "$end",
    "$end",
    "}",
    (const char *)0
};    
    
/** */
static bool hasDef(const string& def,vector<string>& defs) {
    vector<string>::iterator itr;
    for (itr = defs.begin(); itr != defs.end(); ++itr) {
        if (def == *itr) {
            return true;
        }
    }        
    
    return false;
}

/** */
static string preprocess(const char *lines[],vector<string>& defs) {
    vector<bool> consume;
    string str;
    
    int index = 0;
// for each line
    while (lines[index] != (const char *)0) {
        string line = string(lines[index]);
        index++;

        
    // $if
        if (startsWith(line,"$if ")) {
            string def = line.substr(4,line.length() - 4);
            consume.push_back(hasDef(def,defs) == false);
            continue;
        }
        
    // $ifnot
        if (startsWith(line,"$ifnot ")) {
            string def = line.substr(7,line.length() - 7);
            consume.push_back(hasDef(def,defs) == true);
            continue;
        }
        
    // $end
        if (startsWith(line,"$end")) {
            consume.pop_back();
            continue;
        }
        
    // consume?
        bool doConsume = false;
        for (unsigned int index = 0; index < consume.size(); index++) {
            if (consume[index] == true) {
                doConsume = true;
                break;
            }
        }
        if (doConsume == false) {
            str.append(line);
            str.append("\n");
        }
    }
    
    return str;
}    

/** */
void GLDefaultDrawerShader::create() {
// create the program array
    int programsSize = DefaultDrawerFeatures::getMaxIndex() + 1;
    programs = new (nothrow) GLSLProgram*[programsSize];
    if (programs == (GLSLProgram **)0) {
        setNoMemoryError();
        return;
    }
    
// clear the program array
    for (int index = 0; index < programsSize; index++) {
        programs[index] = (GLSLProgram *)0;
    }
}

/** */
string GLDefaultDrawerShader::preprocessShaderSrc(
    DefaultDrawerFeatures& features,const char *lines[]) {
    vector<string> defs;
    
// color
    if (features.hasColor()) {
        defs.push_back("u_Color");
    }
    
// vertex color
    if (features.hasVertexColor()) {
        defs.push_back("a_Color");
    }
    
// texture
    if (features.hasTexture()) {
        defs.push_back("t_Color");
    }
    
    return preprocess(lines,defs);
}
    
/** */
string GLDefaultDrawerShader::getVertShaderSrc(
    DefaultDrawerFeatures& features) {
//
    return preprocessShaderSrc(features,vertShaderSrc);
}

/** */
string GLDefaultDrawerShader::getFragShaderSrc(
    DefaultDrawerFeatures& features) {
//
    return preprocessShaderSrc(features,fragShaderSrc);
}
  
/** */
GLSLShader *GLDefaultDrawerShader::createVertShader(
    DefaultDrawerFeatures& features) {
// create
    GLSLShader *shader = new (nothrow) GLSLShader(GL_VERTEX_SHADER);
    if (shader == (GLSLShader *)0) {
        setNoMemoryError();
        return (GLSLShader *)0;
    }
    if (shader->create() == false) {
        setError(shader->getError());
        delete shader;
        return (GLSLShader *)0;
    }
    string src = getVertShaderSrc(features);
    if (shader->compile(src.c_str()) == false) {
        setError(shader->getError());
        delete shader;
        return (GLSLShader *)0;
    }   
    
    return shader;
}

/** */
GLSLShader *GLDefaultDrawerShader::createFragShader(
    DefaultDrawerFeatures& features) {
// create
    GLSLShader *shader = new (nothrow) GLSLShader(GL_FRAGMENT_SHADER);
    if (shader == (GLSLShader *)0) {
        setNoMemoryError();
        return (GLSLShader *)0;
    }
    if (shader->create() == false) {
        setError(shader->getError());
        delete shader;
        return (GLSLShader *)0;
    }
    string src = getFragShaderSrc(features);
    if (shader->compile(src.c_str()) == false) {
        setError(shader->getError());
        delete shader;
        return (GLSLShader *)0;
    }   
    
    return shader;
}

/** */
GLSLProgram *GLDefaultDrawerShader::getGLSLProgram(
    DefaultDrawerFeatures& features) {
// check if already created
    int programIndex = features.getIndex();
    stringstream mb;
    if (programs[programIndex] != (GLSLProgram *)0) {
        return programs[programIndex];
    }

// vertex shader
    GLSLShader *vertShader = createVertShader(features);
    if (vertShader == (GLSLShader *)0) {
        return (GLSLProgram *)0;
    }
    
// fragment shader
    GLSLShader *fragShader = createFragShader(features);
    if (fragShader == (GLSLShader *)0) {
        delete vertShader;
        return (GLSLProgram *)0;
    }

// program
    GLSLProgram *program = new (nothrow) GLSLProgram();
    if (program == (GLSLProgram *)0) {
        delete vertShader;
        delete fragShader;
        return (GLSLProgram *)0;
    }
    if (program->build(vertShader,fragShader) == false) {
        setError(program->getError());
        delete program;
        delete vertShader;
        delete fragShader;
        return (GLSLProgram *)0;
    }
    
    programs[programIndex] = program;
    return program;
}
    
} // namespace
    
} // namespace