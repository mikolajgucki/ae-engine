#ifndef AE_DESKTOP_IAP_CFG_JSON_READER_H
#define AE_DESKTOP_IAP_CFG_JSON_READER_H

#include <Json/Json.h>

#include "Error.h"
#include "DesktopIAPCfg.h"

namespace ae {

namespace iap {
  
/**
 * \brief Reads a desktop IAP configuration from a JSON value.
 */
class DesktopIAPCfgJSONReader:public Error {
    /** */
    bool readRestorePurchases(DesktopIAPCfg *cfg,Json::Value json);
    
    /** */
    bool readProducts(DesktopIAPCfg *cfg,Json::Value json);
    
public:
    /**
     * \brief Constructs a DesktopIAPCfg.
     */
    DesktopIAPCfgJSONReader():Error() {
    }
    
    /**
     * \brief Reads the configuration.
     * \return The configuration or null if reading fails.
     */
    DesktopIAPCfg *read(Json::Value json);
};
    
} // namespace

} // namespace

#endif // AE_DESKTOP_IAP_CFG_JSON_READER_H