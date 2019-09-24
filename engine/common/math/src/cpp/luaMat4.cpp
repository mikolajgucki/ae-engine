/*
-- @module Mat4
-- @group Math
-- @brief Provides functions for 4x4 matrix manipulation.
-- @full Provides functions for 4x4 matrix manipulation. This module is
--   C library loaded by default.
*/
#include <sstream>
#include <memory>

#include "lua_common.h"
#include "luaVec3.h"
#include "Mat4.h"
#include "luaMat4.h"

using namespace std;
using namespace ae::lua;

namespace ae {
    
namespace math {
    
namespace lua {
    
/// The name of the Lua user type.
static const char *mat4Name = "Mat4";

/// The name of the Lua metatable.
static const char *mat4MetatableName = "Mat4.metatable";

/** */
Mat4Type *checkMat4Type(lua_State *L,int index) {
    void *data = luaL_checkudata(L,index,mat4MetatableName);
    luaL_argcheck(L,data != (void *)0,index,"Mat4 expected");
    return (Mat4Type *)data;
}

/*
-- @name .new
-- @func
-- @brief Creates a matrix with unspecified values.
-- @return The new matrix object.
*/
static int mat4New(lua_State *L) {
    Mat4 *matrix = new (nothrow) Mat4();
    if (matrix == (Mat4 *)0) {
        luaPushNoMemoryError(L);
        return 0;        
    }
    
    Mat4 *ma = new (nothrow) Mat4();
    if (ma == (Mat4 *)0) {
        delete matrix;
        luaPushNoMemoryError(L);
        return 0;        
    }
    
    Mat4 *mb = new (nothrow) Mat4();
    if (mb == (Mat4 *)0) {
        delete ma;
        delete matrix;
        luaPushNoMemoryError(L);
        return 0;
    }
    
// user type
    Mat4Type *data = (Mat4Type *)lua_newuserdata(L,sizeof(Mat4Type));
    data->matrix = matrix;
    data->ma = ma;
    data->mb = mb;
    
// metatable
    luaL_getmetatable(L,mat4MetatableName);
    lua_setmetatable(L,-2);
    
    return 1;
}

/*
-- @name :identity
-- @func
-- @brief Loads the identity matrix.
*/
static int mat4Identity(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);
    data->matrix->identity();
    
    return 0;
}

/*
-- @name :mul
-- @func
-- @brief Multiplies 2 matrices.
-- @full Multiplies 2 matrices and stores the result in this matrix.
-- @param a The first matrix.
-- @param b The second matrix.
*/
static int mat4Mul(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);
    Mat4Type *dataA = checkMat4Type(L,2);
    Mat4Type *dataB = checkMat4Type(L,3);
    
    data->matrix->mul(dataA->matrix,dataB->matrix);
    return 0;
}

/*
-- @name :translate
-- @func
-- @brief Loads a 2D translation matrix (Z coordinate equal to 0).
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
-- @func
-- @brief Loads a 3D translation matrix.
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
-- @param z The translation along the Z axis.
*/
static int mat4Translate(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    Mat4ElementType x = (Mat4ElementType)luaL_checknumber(L,2);
    Mat4ElementType y = (Mat4ElementType)luaL_checknumber(L,3);
    
    Mat4ElementType z = 0;
    if (lua_isnoneornil(L,4) == 0) {
        z = (Mat4ElementType)luaL_checknumber(L,4);
    }
    
    data->matrix->translate(x,y,z);
    return 0;
}

/*
-- @name :mulTranslate
-- @func
-- @brief Multiplies this matrix by a 2D translation matrix (Z coordinate
--   equal to 0).
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
-- @func
-- @brief Multiplies this matrix by a 3D translation matrix.
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
-- @param z The translation along the Z axis.
*/
static int mat4MulTranslate(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    Mat4ElementType x = (Mat4ElementType)luaL_checknumber(L,2);
    Mat4ElementType y = (Mat4ElementType)luaL_checknumber(L,3);
    
    Mat4ElementType z = 0;
    if (lua_isnoneornil(L,4) == 0) {
        z = (Mat4ElementType)luaL_checknumber(L,4);
    }
    
    data->ma->assign(data->matrix);
    data->mb->translate(x,y,z);
    data->matrix->mul(data->ma,data->mb);
    
    return 0;
}

/*
-- @name :scale
-- @func
-- @brief Loads a 2D scale matrix (Z factor equal to 1).
-- @param sx The scale factor along the X axis.
-- @param sy The scale factor along the Y axis.
-- @func
-- @brief Loads a 3D scale matrix.
-- @param sx The scale factor along the X axis.
-- @param sy The scale factor along the Y axis.
-- @param sz The scale factor along the Z axis.
*/
static int mat4Scale(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    Mat4ElementType sx = (Mat4ElementType)luaL_checknumber(L,2);
    Mat4ElementType sy = (Mat4ElementType)luaL_checknumber(L,3);
    
    Mat4ElementType sz = 1;
    if (lua_isnoneornil(L,4) == 0) {
        sz = (Mat4ElementType)luaL_checknumber(L,4);
    }
    
    data->matrix->scale(sx,sy,sz);
    return 0;
}


/*
-- @name :mulScale
-- @func
-- @brief Multiplies this matrix by a 2D scale matrix (Z factor equal to 1).
-- @param sx The scale factor along the X axis.
-- @param sy The scale factor along the Y axis.
-- @func
-- @brief Multiplies this matrix by a 3D scale matrix.
-- @param sx The scale factor along the X axis.
-- @param sy The scale factor along the Y axis.
-- @param sz The scale factor along the Z axis.
*/
static int mat4MulScale(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    Mat4ElementType sx = (Mat4ElementType)luaL_checknumber(L,2);
    Mat4ElementType sy = (Mat4ElementType)luaL_checknumber(L,3);
    
    Mat4ElementType sz = 1;
    if (lua_isnoneornil(L,4) == 0) {
        sz = (Mat4ElementType)luaL_checknumber(L,4);
    }
    
    data->ma->assign(data->matrix);
    data->mb->scale(sx,sy,sz);
    data->matrix->mul(data->ma,data->mb);
    
    return 0;
}

/*
-- @name :translateAndScale
-- @func
-- @brief Loads a matrix which translates and scales.
-- @param x The translation along the X axis.
-- @param y The translation along the Y axis.
-- @param z The translation along the Z axis.
-- @param sx The scale factor along the X axis.
-- @param sy The scale factor along the Y axis.
-- @param sz The scale factor along the Z axis.
*/
static int mat4TranslateAndScale(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    Mat4ElementType x = (Mat4ElementType)luaL_checknumber(L,2);
    Mat4ElementType y = (Mat4ElementType)luaL_checknumber(L,3);
    Mat4ElementType z = (Mat4ElementType)luaL_checknumber(L,4);
    
    Mat4ElementType sx = (Mat4ElementType)luaL_checknumber(L,5);
    Mat4ElementType sy = (Mat4ElementType)luaL_checknumber(L,6);
    Mat4ElementType sz = (Mat4ElementType)luaL_checknumber(L,7);
       
    data->matrix->translateAndScale(x,y,z,sx,sy,sz);
    return 0;    
}

/*
-- @name :rotateOZ
-- @func
-- @brief Loads a matrix which rotates about the Z axis.
-- @param angle The angle in radians.
*/
static int mat4RotateOZ(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    double angle = luaL_checknumber(L,2);

    data->matrix->rotateOZ(angle);
    return 0;    
}

/*
-- @name :mulRotateOZ
-- @func
-- @brief Multiplies this matrix by a rotation matrix about the Z axis.
-- @param angle The angle in radians.
*/
static int mat4MulRotateOZ(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);    
    double angle = luaL_checknumber(L,2);

    data->ma->assign(data->matrix);    
    data->mb->rotateOZ(angle);
    data->matrix->mul(data->ma,data->mb);
    
    return 0;    
}
/*
-- @name :mulVec3
-- @func
-- @brief Multiplies a 3D vector by this matrix.
-- @param vector The vector to multiply.
-- @param result The vector in which to store the result.
*/
static int mat4MulVec3(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);
    
// vector, result
    Vec3Type *vector = checkVec3Type(L,2);
    Vec3Type *result = checkVec3Type(L,3);
    
// multiply
    data->matrix->mul(*vector->vec,*result->vec);
    
    return 0;    
}

/*
-- @name :__tostring
-- @func
-- @brief Gets the string representation of the matrix.
-- @return The string representation of the matrix.
*/
static int mat4__tostring(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);
    ostringstream str;
    
    for (int i = 1; i <= 4; i++) {
        for (int j = 1; j <= 4; j++) {
            str << data->matrix->get(i,j) << " ";
        }
        str << "\n";
    }
    
    lua_pushstring(L,str.str().c_str());
    return 1;
}

/*
-- @name :__gc
-- @func
-- @brief Destroys the matrix.
-- @full Destroys the matrix. Never call this function directly.
*/
static int mat4__gc(lua_State *L) {
    Mat4Type *data = checkMat4Type(L);
    delete data->matrix;
    
    return 0;
}

/** The type functions. */
static const struct luaL_Reg mat4Funcs[] = {    
    {"new",mat4New},
    {0,0}
};

/** The type methods. */
static const struct luaL_Reg mat4Methods[] = {
    {"identity",mat4Identity},
    {"mul",mat4Mul},
    {"translate",mat4Translate},
    {"mulTranslate",mat4MulTranslate},
    {"scale",mat4Scale},
    {"mulScale",mat4MulScale},
    {"translateAndScale",mat4TranslateAndScale},
    {"rotateOZ",mat4RotateOZ},
    {"mulRotateOZ",mat4MulRotateOZ},
    {"mulVec3",mat4MulVec3},
    {0,0}
};

/** The type metamethods. */
static const struct luaL_Reg mat4Metamethods[] = {
    {"__tostring",mat4__tostring},
    {"__gc",mat4__gc},
    {0,0}
};

/** */
static int mat4RequireFunc(lua_State *L) {
    luaLoadUserType(L,mat4MetatableName,mat4Funcs,mat4Methods,mat4Metamethods);
    return 1;
}

/** */
void loadMat4Lib(lua_State *L) {
    luaL_requiref(L,mat4Name,mat4RequireFunc,1);
    lua_pop(L,1);
}
    
} // namespace
    
} // namespace
    
} // namespace