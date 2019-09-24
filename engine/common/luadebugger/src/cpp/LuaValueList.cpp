#include "LuaValueList.h"

using namespace std;

namespace ae {

namespace lua {
 
/** */
LuaValueList::~LuaValueList() {
    vector<LuaValue *>::iterator itr;
    for (itr = values.begin(); itr != values.end(); ++itr) {
        LuaValue *value = *itr;
        delete value;
    }
}
    
} // namespace

} // namespace