#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#include "Log.h"
#include "AENSURLConnectionDelegate.h"
#include "AEiOSLuaLibHTTP.h"

using namespace std;
using namespace ae;
using namespace ae::http;

/// The log tag.
static const char *logTag = "AEHTTP";

/** */
void AEiOSLuaLibHTTP::create() {
    session = [NSURLSession sessionWithConfiguration:
        [NSURLSessionConfiguration defaultSessionConfiguration]];
}

/** */
void AEiOSLuaLibHTTP::get(const string& requestId,const string& url) {
// log
    ostringstream msg;
    msg << "AEiOSLuaLibHTTP::get(" << requestId << "," << url << ")";
    Log::trace(logTag,msg.str());
    
// URL
    const char *urlCStr = url.c_str();
    NSString *nsUrlStr = [NSString stringWithUTF8String:urlCStr];
    NSURL *nsUrl = [NSURL URLWithString:nsUrlStr];
    
// callback
    HTTPCallback *callback = getCallback();
    
// identifier
    NSString *nsRequestId = [NSString stringWithUTF8String:requestId.c_str()];
    
// connect and send request
    NSURLSessionDataTask *task = [session dataTaskWithURL:nsUrl
        completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
            Log::trace(logTag,"completionHandler() called");
            if (error != nil) {
                callback->failedToReceiveResponse([nsRequestId UTF8String],
                    [[error localizedDescription] UTF8String]);
                return;
            }
        
            NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;
            NSString *dataStr = [[NSString alloc]
                initWithData:data encoding:NSUTF8StringEncoding];
            callback->receivedResponse([nsRequestId UTF8String],
                (int)[httpResponse statusCode],[dataStr UTF8String]);
        }];
    [task resume];
}

/** */
void AEiOSLuaLibHTTP::openURL(const string& url) {
    NSString *nsUrlStr = [NSString stringWithUTF8String:url.c_str()];
    NSURL *nsUrl = [NSURL URLWithString:nsUrlStr];
    if ([[UIApplication sharedApplication] canOpenURL:nsUrl]) {
        [[UIApplication sharedApplication] openURL:nsUrl];
    }
}