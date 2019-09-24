#include <sstream>

#include "TimerAlarm.h"
#include "DesktopLuaLibHTTP.h"

using namespace std;
using namespace ae;
using namespace ae::engine;

namespace ae {
    
namespace http {
 
/**
 * \brief Fires response on HTTP GET.
 */
class HTTPGetAlarm:public TimerAlarm {
    /// The library.
    DesktopLuaLibHTTP *luaLibHttp;
    
    /// The request identifier.
    const string id;
    
    /// The URL.
    const string url;
    
public:
    /** */
    HTTPGetAlarm(DesktopLuaLibHTTP *luaLibHttp_,int time_,const string &id_,
        const string &url_):TimerAlarm(time_),luaLibHttp(luaLibHttp_),id(id_),
        url(url_) {
    }
    
    /** */
    virtual ~HTTPGetAlarm() {
    }
    
    /** */
    virtual void fire() {
        luaLibHttp->sendGetResponse(id,url);
        delete this;
    }
};
    
/// The log tag.
static const char *logTag = "Desktop/HTTP";

/** */
DesktopLuaLibHTTP::~DesktopLuaLibHTTP() {
    for (unsigned int index = 0; index < responses.size(); index++) {
        delete responses[index];
    }
}

/** */
DesktopHTTPResponse *DesktopLuaLibHTTP::getResponse(const string& url) {
    for (unsigned int index = 0; index < responses.size(); index++) {
        DesktopHTTPResponse *response = responses[index];
        if (response->getURL() == url) {
            return response;
        }
    }        
        
    return (DesktopHTTPResponse *)0;
}

/** */
bool DesktopLuaLibHTTP::parseResponse(Json::Value json) {
// url
    const char *urlKey = "url";
    if (json.isMember(urlKey) == false) {
        ostringstream err;
        err << "Response without URL (missing value of key " << urlKey << ")";
        setError(err.str());
        return false;   
    }
    if (json[urlKey].isString() == false) {
        ostringstream err;
        err << "JSON value of key " << urlKey << " is not a string";
        setError(err.str());
        return false;   
    }
    
// response
    DesktopHTTPResponse *response = new DesktopHTTPResponse(
        json[urlKey].asString());
    responses.push_back(response);
    
// code
    const char *codeKey = "code";
    if (json.isMember(codeKey) == true) {
        if (json[codeKey].isInt() == false) {
            ostringstream err;
            err << "JSON value of key " << codeKey << " is not an integer";
            setError(err.str());
            return false;   
        }        
        response->setCode(json[codeKey].asInt());
    }
    
// body
    const char *bodyKey = "body";
    if (json.isMember(bodyKey) == true) {
        if (json[bodyKey].isString() == false) {
            ostringstream err;
            err << "JSON value of key " << bodyKey << " is not a string";
            setError(err.str());
            return false;   
        }          
        response->setBody(new string(json[bodyKey].asString()));
    }
    
// error
    const char *errorKey = "error";
    if (json.isMember(errorKey) == true) {
        if (json[errorKey].isString() == false) {
            ostringstream err;
            err << "JSON value of key " << errorKey << " is not a string";
            setError(err.str());
            return false;   
        }          
        response->setError(new string(json[errorKey].asString()));
    }

    return true;    
}

/** */
bool DesktopLuaLibHTTP::initLuaLib() {
    Json::Value json;
    if (cfg->readJSONCfg(json) == false) {
        setError(cfg->getError());
        return false;
    }
    
// responses
    const char *responsesKey = "responses";
    if (json.isMember(responsesKey)) {
        if (json[responsesKey].isArray() == false) {
            ostringstream err;
            err << "JSON value of key " << responsesKey <<
                " is not an array";
            setError(err.str());
            return false;   
        }
        
        Json::Value responsesValue = json[responsesKey];
        for (unsigned int index = 0; index < responsesValue.size(); index++) {
            parseResponse(responsesValue[index]);
        }
    }
    
    return true;
}

/** */
void DesktopLuaLibHTTP::get(const string& id,const string& url) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibHTTP::get(" << id << "," << url << ")";
    log->trace(logTag,msg.str());
 
// alarm
    HTTPGetAlarm *alarm = new HTTPGetAlarm(this,3000,id,url);
    timer->addAlarm(alarm);
}

/** */
void DesktopLuaLibHTTP::openURL(const std::string& url) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibHTTP::openURL(" << url << ")";
    log->trace(logTag,msg.str());
}

/** */
void DesktopLuaLibHTTP::sendGetResponse(const string& id,const string& url) {
// log
    ostringstream msg;
    msg << "DesktopLuaLibHTTP::sendGetResponse(" << id << "," << url << ")";
    log->trace(logTag,msg.str());
    
    DesktopHTTPResponse *response = getResponse(url);
// not found
    if (response == (DesktopHTTPResponse *)0) {
        getCallback()->receivedResponse(id,404,"");
        return;
    }
    
// error
    const string *error = response->getError();
    if (error != (const string *)0) {
        getCallback()->failedToReceiveResponse(id,*error);
        return;
    }
    
// body
    const string *body = response->getBody();
    if (body != (const string *)0) {
        getCallback()->receivedResponse(id,response->getCode(),*body);
    }
    else {
        getCallback()->receivedResponse(id,response->getCode(),"");
    }    
}
    
} // namespace
    
} // namespace