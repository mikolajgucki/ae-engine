#ifndef AE_LUAP_CALL_H
#define AE_LUAP_CALL_H

/**
 * \brief Represents a Lua call.
 */
typedef struct {
    /** */
    const char *type;
    
    /** */
    char *name;
    
    /** */
    char *source;
    
    /** */
    int linedef;   
    
    /** */
    long hits;
    
    /** */
    long totalTime;
    
    /** */
    long totalMemoryAllocated;
    
    /** */
    long subcallsMemoryAllocated;
} luap_Call;

/** */
luap_Call *luap_CreateCall(const char *type,const char *name,
    const char *source,int linedef);

/** */
void luap_DeleteCall(luap_Call *call);

#endif // AE_LUAP_CALL_H