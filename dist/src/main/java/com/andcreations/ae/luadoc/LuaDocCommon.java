package com.andcreations.ae.luadoc;

/**
 * Contains common methods for all the LuaDoc classes.
 *
 * @author Mikolaj Gucki
 */
class LuaDocCommon {
    /**
     * Prefixes the syntax with the module name if it starts with dot or
     * colon.
     *
     * @param module The module.
     * @param syntax The syntax.
     * @return The new syntax.
     */
    static String updateSyntax(LuaDocModule module,String syntax) {
        if (syntax.startsWith(".") || syntax.startsWith(":")) {
            return module.getName() + syntax;
        }
        
        return syntax;
    }
}