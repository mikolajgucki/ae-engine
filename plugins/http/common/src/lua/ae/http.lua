-- @brief The HTTP client.
local _http = http
local http = {}

-- the identifier sequence
local idSequence = 0

-- the table with requests in which request identifier is the key and
-- the callback functions is the value
local requests = {}

-- @brief Gets the next identifier.
-- @return The next identifier.
local function nextId()
    idSequence = idSequence + 1
    return idSequence
end

-- @brief Sets data associated with a request.
-- @param id The request identifer.
-- @param data The data.
local function setRequestData(id,data)
    requests[id] = data
end

-- @brief Gets data associated with a request.
-- @param id The request identifer.
-- @return The data.
local function getRequestData(id)
    local funcs = requests[id]
    requests[id] = nil
    return funcs
end

-- @brief Called when a response has been received.
-- @param id The request identifier.
-- @param code The response code.
-- @param body The response body.
function _http.receivedResponse(id,code,body)
    local data = getRequestData(id)
    if data.funcs.responseFunc then
        data.funcs.responseFunc(data.url,code,body)
    end
end

-- @brief Called when it failed to receive a response.
-- @param id The request identifier.
-- @param msg The error message.
function _http.failedToReceiveResponse(id,msg)
    local data = getRequestData(id)
    if data.funcs.failedToReceiveResponse then
        data.funcs.failedToReceiveResponse(data.url,msg)
    end
end

-- @brief Sends a HTTP GET request.
-- @full Sends a HTTP GET request. The functions table  
-- @param url The URL.
-- @param funcs The table of functions called when a response is received or
--   error occurs.
function http.get(url,funcs)
    local id = string.format('http.get.%d',nextId())
    _http.get(id,url)
    setRequestData(id,{
        url = url,
        funcs = funcs
    })
end

-- @brief Encodes a URL.
-- @param url The URL to encode.
-- @return The encoded URL.
function http.encodeURL(url)
   if url then
       url = string.gsub(url,"\n","\r\n")
       url = string.gsub(url,"([^%w ])",function(ch)
           return string.format("%%%02X",string.byte(ch))
       end)
       url = string.gsub(url," ","+")
   end
   return url
end

ae = ae or {}
ae.http = http