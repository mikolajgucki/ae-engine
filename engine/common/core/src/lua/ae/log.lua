-- @brief Logs message format using `string.format`.

-- null logger if none set
if not log then
    log = {
        trace = function()
        end,
        debug = function()
        end,
        info = function()
        end,
        warning = function()
        end,
        error = function()
        end
    }
end

-- keep the native library
local _log = log

-- the new library
local log = {}

-- @func
-- @brief Logs a trace message.
-- @param message The message.
-- @func
-- @brief Logs a trace message.
-- @param message The message.
-- @param args The list of format arguments.
function log.trace(...)
    _log.trace(string.format(...))
end

-- @func
-- @brief Logs a debug message.
-- @param message The message.
-- @func
-- @brief Logs a debug message.
-- @param message The message.
-- @param args The list of format arguments.
function log.debug(...)
    _log.debug(string.format(...))
end

-- @func
-- @brief Logs an info message.
-- @param message The message.
-- @func
-- @brief Logs an info message.
-- @param message The message.
-- @param args The list of format arguments.
function log.info(...)
    _log.info(string.format(...))
end

-- @func
-- @brief Logs a warning message.
-- @param message The message.
-- @func
-- @brief Logs a warning message.
-- @param message The message.
-- @param args The list of format arguments.
function log.warning(...)
    _log.warning(string.format(...))
end

-- @func
-- @brief Logs an error message.
-- @param message The message.
-- @func
-- @brief Logs an error message.
-- @param message The message.
-- @param args The list of format arguments.
function log.error(...)
    _log.error(string.format(...))
end

return log