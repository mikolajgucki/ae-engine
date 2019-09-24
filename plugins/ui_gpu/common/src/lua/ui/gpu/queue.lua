-- @brief The default GPU command queue.
local queue = {}

-- @brief The GPU queue.
local gpuQueue = gpu.GPUQueue.new()

-- @brief The commands.
local commands = {}

-- @brief Gets the GPU queue.
-- @return The GPU queue.
function queue.getGPUQueue()
    return gpuQueue
end

-- @brief Enqueues a command.
-- @param command The command.
function queue.enqueue(command)
    ae.itable.append(commands,command)
end

-- @brief Removes a command from the queue.
-- @param command The command to remove.
function queue.remove(command)
    ae.itable.remove(commands,command)
end

-- @brief Flushes the queue.
function queue.flush()
    for index = 1,#commands do
        local command = commands[index]
        command.execute(gpuQueue,command)
    end
    gpuQueue:flush()
end

-- @brief Removes all the commands from the queue.
function queue.clean()
    commands = {}
end

return queue