#include "AENSURLConnectionDelegate.h"

using namespace ae::http;

@implementation AENSURLConnectionDelegate

/** */
-(id)initWithRequestId:(NSString *)requestId
    withHttpCallback:(HTTPCallback *)httpCallback {
//
    self = [super init];
    if (self) {
        _requestId = requestId;
        _httpCallback = httpCallback;
        _responseData = nil;
    }
    return self;
}

/** */
-(void)connection:(NSURLConnection *)connection
    didReceiveResponse:(NSURLResponse *)response {
//
    _responseData = [[NSMutableData alloc] init];
    NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
    _responseCode = [httpResponse statusCode];
}

/** */
-(void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [_responseData appendData:data];
}
 
/** */
-(NSCachedURLResponse *)connection:(NSURLConnection *)connection
    willCacheResponse:(NSCachedURLResponse*)cachedResponse {
//
    return nil;
}

 
/** */
-(void)connectionDidFinishLoading:(NSURLConnection *)connection {
    NSString *dataStr = [[NSString alloc]
        initWithData:_responseData encoding:NSUTF8StringEncoding];
    _httpCallback->receivedResponse([_requestId UTF8String],(int)_responseCode,
        [dataStr UTF8String]);
}
 
/** */
-(void)connection:(NSURLConnection *)connection
    didFailWithError:(NSError *)error {
//
    NSString *errorStr = [error localizedDescription];
    _httpCallback->failedToReceiveResponse([_requestId UTF8String],
        [errorStr UTF8String]);
}

@end