#import <Foundation/Foundation.h>
#include "HTTPCallback.h"

@interface AENSURLConnectionDelegate : NSObject<NSURLConnectionDelegate>

// The request identifier.
@property (readonly) NSString *requestId; 

// The callback.
@property ae::http::HTTPCallback *httpCallback;

// The response code.
@property NSInteger responseCode;

// The response data.
@property NSMutableData *responseData;

// Initializes the delegate with a request identifier.
-(id)initWithRequestId:(NSString *)requestId
      withHttpCallback:(ae::http::HTTPCallback *)httpCallback;

@end