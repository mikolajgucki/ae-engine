#ifndef AE_ENGINE_KEY_CODES_H
#define AE_ENGINE_KEY_CODES_H

namespace ae {
    
namespace engine {
  
/**
 * \brief Represents a key.
 */
class Key {
    /// The key code.
    const int code;
    
    /// The key name.
    const char *name;
    
public:
    /**
     * \brief Constructs a Key.
     * \param code_ The key unique code.
     * \param name_ The key unique name.
     */
    Key(int code_,const char *name_):code(code_),name(name_) {
    }
    
    /**
     * \brief Gets the key code.
     * \return The key code.
     */
    int getCode() const {
        return code;
    }
    
    /**
     * \brief Gets the key name.
     * \return The key name.
     */
    const char *getName() const {
        return name;
    }
    
    /// The back key.
    static const Key BACK;
    
    /// The menu key.
    static const Key MENU;
    
    /// All the key codes.
    static const Key *KEYS[];
    
    /**
     * \brief Finds a key by name.
     * \return The key or null if there is no such key.
     */
    static const Key *findKeyByName(const char *name);
};
    
} // namespace
    
} // namespace

#endif // AE_ENGINE_KEY_CODES_H