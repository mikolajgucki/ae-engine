-- @brief Lua unit test runner.
-- @group Test
local runner = {}

-- @brief Tests if a string starts with a prefix.
-- @param str The string.
-- @param prefix The prefix.
-- @return `true` if the string starts with the prefix, `false` otherwise.
local function startsWith(str,prefix)
    return str:sub(1,prefix:len()) == prefix
end


-- @brief Reports an error.
-- @param listener The runner listener.
-- @param errorFuncName The name of function to call on the listener.
-- @param moduleName The module name.
-- @param testName The test name.
-- @param ... The other arguments passed to the listener.
local function reportError(listener,errorFuncName,moduleName,testName,...)
    if listener and listener[errorFuncName] then
        listener[errorFuncName](moduleName,testName,...)
    end
end

-- @brief Handles errors.
-- @param msg The error msg.
local function testRunnerErrorHandler(msg)
    errorMsg = msg or '<no error message>'
    errorTraceback = debug.traceback()
end

-- @brief Runs a test case.
-- @param moduleName The module name.
-- @param testName The test name.
-- @param func The function to run as the test case.
-- @param listener The runner listener.
-- @param setUp The set-up function.
-- @param tearDown The tear-down function.
local function runTestCase(moduleName,testName,func,listener,setUp,tearDown)
    -- notify running
    if listener and listener.runningTest then   
        listener.runningTest(moduleName,testName)
    end
    
    -- not a function
    if type(func) ~= 'function' then
        reportError(listener,'notFunctionError',moduleName,testName)
        return
    end
    local result,msg
    local failed = false
    
    -- set up
    if setUp then
        result,msg = pcall(setUp)
        if not result then
            reportError(listener,'setUpFailed',moduleName,testName,msg)
            return
        end
    end
    
    -- test
    result = xpcall(func,testRunnerErrorHandler)
    if not result then
        reportError(listener,'testFailed',moduleName,testName,errorMsg)
        reportError(listener,'tracebackCaught',
            moduleName,testName,errorTraceback,errorMsg)
        failed = true
    end
    
    -- tear down
    if tearDown then
        result,msg = pcall(tearDown)
        if not result and not failed then
            reportError(listener,'tearDownFailed',moduleName,testName,msg)
            return
        end
    end    
    
    -- notify finished
    if not failed and listener and listener.testFinished then
        listener.testFinished(moduleName,testName)
    end
end

-- @brief Runs a test suite.
-- @param moduleName The name of the module with tests.
-- @param listener The runner listener.
local function runTestSuite(moduleName,listener)
    -- notify
    if listener and listener.runningTestSuite then
        listener.runningTestSuite(moduleName,module)
    end
    
    -- module
    local module = require(moduleName)
    if type(module) ~= 'table' then
        error(string.format('Value returned by %s is not a table',moduleName))
    end
    
    -- set up
    local setUp = module['setUp']
    if type(setUp) ~= 'function' then
        setUp = nil
    end
    
    -- tear down
    local tearDown = module['tearDown']
    if type(tearDown) ~= 'function' then
        tearDown = nil
    end
    
    local testNames = {}
    -- look for test functions
    for key,value in pairs(module) do
        if startsWith(key,"test") then
            table.insert(testNames,key)
        end
    end
    
    table.sort(testNames)
    -- run the tests
    for _,testName in ipairs(testNames) do
        local testFunc = module[testName]
        runTestCase(moduleName,testName,testFunc,listener,setUp,tearDown)
    end
    
    -- notify
    if listener and listener.testSuiteFinished then
        listener.testSuiteFinished(moduleName,module)
    end    
end

-- @brief Runs all test suites by names.
-- @param moduleNames The names of the modules with tests.
-- @param listener The runner listener.
function runner.runAllByName(moduleNames,listener)
    for _,moduleName in ipairs(moduleNames) do
        local result,msg = pcall(runTestSuite,moduleName,listener)
        if not result and listener and listener.failedToRunTestSuite then
            listener.failedToRunTestSuite(moduleName,msg)
        end
    end
end

-- @brief Fails a test.
-- @param msg The identiyfing message.
function fail(msg)
    error(msg)
end

-- @brief Asserts that a condition is true.
-- @param condition The condition.
-- @param msg The identiyfing message.
function assertTrue(condition,msg)
    if not condition then
        fail(msg)
    end
end

-- @brief Asserts that a condition is false.
-- @param condition The condition.
-- @param msg The identiyfing message.
function assertFalse(condition,msg)
    if condition then
        fail(msg)
    end
end

-- @brief Asserts that two values are equal (`=`).
-- @param a The first value.
-- @param b The second value.
-- @param msg The identiyfing message.
function assertEquals(a,b,msg)
    if a ~= b then
        fail(msg)
    end
end

-- @brief Asserts that two values are not equal (`=`).
-- @param a The first value.
-- @param b The second value.
-- @param msg The identiyfing message.
function assertNotEquals(a,b,msg)
    if a == b then
        fail(msg)
    end
end

-- @brief Asserts that a table has given size.
-- @param tbl The table.
-- @param expectedSize The expected size.
-- @param msg The identiyfing message.
function assertSize(tbl,expectedSize,msg)
    if type(tbl) ~= 'table' then
        fail(string.format('Not a table [%s]',msg))
    end

    local actualSize = 0
    for k,v in pairs(tbl) do
        actualSize = actualSize + 1
    end

    if expectedSize ~= actualSize then
        fail(string.format('%s [actual %d, expected %d]',
            msg,actualSize,expectedSize))
    end
end

-- @brief Assets that a value is of given type.
-- @param value The value.
-- @param expectedType The expected type.
-- @param msg The identiyfing message.
function assertType(value,expectedType,msg)
    local actualType = type(value)
    if actualType ~= expectedType then
        fail(string.format('%s [actual %s, expected %s]',msg,
            tostring(actualType),tostring(expectedType)))
    end
end

-- @brief Asserts that a value is nil.
-- @param value The value.
-- @param msg The identiyfing message.
function assertNil(value,msg)
    if value then
        fail(msg)
    end
end

-- @brief Asserts that a value is not nil.
-- @param value The value.
-- @param msg The identiyfing message.
function assertNotNil(value,msg)
    if not value then
        fail(msg)
    end
end

-- @brief Asserts that a value is a number.
-- @param value The value.
-- @param msg The identiyfing message.
function assertNumber(value,msg)
    assertType(value,'number',msg)
end

-- @brief Asserts that a value is a string.
-- @param value The value.
-- @param msg The identiyfing message.
function assertString(value,msg)
    assertType(value,'string',msg)
end

-- @brief Asserts that a value is a boolean.
-- @param value The value.
-- @param msg The identiyfing message.
function assertBoolean(value,msg)
    assertType(value,'boolean',msg)
end

-- @brief Asserts that a value is a table.
-- @param value The value.
-- @param msg The identiyfing message.
function assertTable(value,msg)
    assertType(value,'table',msg)
end

-- @brief Asserts that a value is a function.
-- @param value The value.
-- @param msg The identiyfing message.
function assertFunction(value,msg)
    assertType(value,'function',msg)
end

-- @brief Assets that a function call fails.
-- @param func The function to call.
-- @param msg The identiyfing message.
function assertFails(func,msg)
    local result = pcall(func)
    if result then
        fail(msg)
    end
end

return runner