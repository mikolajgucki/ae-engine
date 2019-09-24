#include "LuaTraceback.h"

using namespace std;

namespace ae {
    
namespace lua {

/** */    
LuaTraceback::~LuaTraceback() {
    vector<LuaTracebackItem *>::iterator itr;
    for (itr = items.begin(); itr != items.end(); ++itr) {
        LuaTracebackItem *item = *itr;
        delete item;
    }
}
    
} // namespace

} // namespace