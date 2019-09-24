#ifndef AE_LOCK_H
#define AE_LOCK_H

namespace ae {
    
/**
 * \brief Locks an engine-wide lock. This lock should be used to synchronize
 *   sections very rarely, typically to synchronize events coming from threads
 *   other that the main one.
 * \return <code>true<code> on success, <code>false</code> otherwise
 */
bool aeGlobalLock();

/**
 * \brief Unlocks an engine-wide lock.
 * \return <code>true<code> on success, <code>false</code> otherwise
 */
bool aeGlobalUnlock();
    
} // namespace

#endif // AE_LOCK_H