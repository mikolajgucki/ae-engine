#include "Log.h"
#include "AESKProductsRequestDelegate.h"

@implementation AESKProductsRequestDelegate

/// The log tag.
static const char *logTag = "AEIAP";

/** */
-(void)productsRequest:(SKProductsRequest *)request 
    didReceiveResponse:(SKProductsResponse *)response {
//
    ::ae::Log::trace(logTag,"AESKProductsRequestDelegate::didReceiveResponse()");
    _productsListener->receivedProducts(response);
}

@end