-- @group UI
-- @brief Lays out component in a grid (left-to-right, bottom-to-top).
local GridLayout,super = ae.oo.subclass(ui.layout.Layout)
    
-- @func
-- @brief Creates a GridLayout object.
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
-- @return A new GridLayout object.
-- @func
-- @brief Creates a GridLayout object.
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
-- @param absolute Indicates if columns and rows are absolute.
-- @return A new GridLayout object. 
function GridLayout.new(columns,rows,absolute)
    local self = ae.oo.new(GridLayout)
    GridLayout.construct(self,columns,rows,absolute)
    
    return self
end

-- @brief Parses sizes (columns,rows)
-- @param sizes The sizes to parse.
-- @return The parsed sizes as table.
function parseSizes(sizes)
    if ae.types.isstring(sizes) then
        local result = {}
        -- for each size in the string
        for size in string.gmatch(sizes,'[^,]+') do
            -- percent
            if ae.string.endsWith(size,'%') then
                local ratioStr = string.sub(size,1,string.len(size) - 1)
                local ratioPercent = tonumber(ratioStr)
                if not ratioPercent then
                    error(string.format('Invalid size %s',ratioStr))
                end
            
                ae.itable.append(result,{
                    ratio = ratioPercent / 100
                })
            -- fixed
            else 
                local fixed = tonumber(size)
                if not fixed then
                    error(string.format('Invalid size %s',size))
                end
                ae.itable.append(result,{
                    fixed = fixed
                })
            end
        end
        
        return result
    end
    
    if ae.types.istable(sizes) then
        local result = {}
        for _,size in ipairs(sizes) do
            ae.itable.append(result,{
                fixed = size
            })
        end
        return result
    end
    
    error(string.format('%s is an unsupported sizes type',type(sizes)))
end

-- @brief Fits columns in the full bounds.
-- @full Fits columns in the full bounds (multiplies fixes sizes by the full
--   bounds width).
-- @param columns The columns.
local function fitColumnsInFullBounds(columns)
    for _,entry in pairs(columns) do
        if entry.fixed then
            entry.fixed = ui.Bounds.full:getWidth(entry.fixed)
        end
    end
end

-- @brief Fits rows in the full bounds.
-- @full Fits rows in the full bounds (multiplies fixes sizes by the full
--   bounds height).
-- @param rows The rows.
local function fitRowsInFullBounds(rows)
    for _,entry in pairs(rows) do
        if entry.fixed then
            entry.fixed = ui.Bounds.full:getHeight(entry.fixed)
        end
    end
end

-- @func
-- @brief Constructs a GridLayout object.
-- @param self The object. 
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
-- @func
-- @brief Constructs a GridLayout object.
-- @param self The object. 
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
-- @param absolute Indicates if columns and rows are absolute.
function GridLayout.construct(self,columns,rows,absolute)
    super.construct(self)
    self.columns = parseSizes(columns)
    self.rows = parseSizes(rows)
    self.absolute = absolute
    
    -- fit in the full bounds if not already absolute
    if not self.absolute then
        fitColumnsInFullBounds(self.columns)
        fitRowsInFullBounds(self.rows)
    end
    
    -- grid
    self.grid = ui.Grid.new(self.columns,self.rows)
    
    -- cells with components
    self.cells = {}
end

-- @brief Finds a component in a cell.
-- @param self The grid layout.
-- @param x The X coordinate of the cell.
-- @param y The Y coordinate of the cell.
-- @return The component in the cell.
local function findComponent(self,x,y)
    for _,cell in ipairs(self.cells) do
        if x >= cell.x and x < cell.x + cell.xspan and
           y >= cell.y and y < cell.y + cell.yspan then
        --
            return cell
        end
    end
end

-- @brief Verifies that no component is set.
-- @param self The grid layout.
-- @param x The X coordinate of the cell.
-- @param y The Y coordinate of the cell.
-- @param xspan The number of cells along the X axis.
-- @param yspan The number of cells along the Y axis.
local function verifyNoComponent(self,x,y,xspan,yspan)
    for ix = 0,xspan - 1 do
        for iy = 0,yspan - 1 do
            local cell = findComponent(self,x + ix,y + iy)
            if cell then
                error(string.format(
                    'Component already set in cell %d,%d (id=%s)',
                    x,y,cell.component.id))
            end
        end
    end
end

-- @func
-- @brief Adds a component.
-- @param component The component.
-- @param x The X coordinate of the cell.
-- @param y The Y coordinate of the cell.
-- @return The layout.
-- @func
-- @brief Adds a component.
-- @param component The component.
-- @param x The zero-based X coordinate of the cell.
-- @param y The zero-based Y coordinate of the cell.
-- @param xspan The number of cells along the X axis.
-- @param yspan The number of cells along the Y axis.
-- @return The layout.
function GridLayout:addComponent(component,x,y,xspan,yspan)
    xspan = xspan or 1
    yspan = yspan or 1
    verifyNoComponent(self,x,y,xspan,yspan)
    
    -- append to the container
    super.append(self,component)
    
    -- keep in the cells
    ae.itable.append(self.cells,{
        component = component,
        x = x,
        y = y,
        xspan = xspan,
        yspan = yspan
    })
    
    return self
end

-- @brief Gets a component in a cell.
-- @param x The X coordinate of the cell.
-- @param y The Y coordinate of the cell.
-- @return The component in the cell.
function GridLayout:getComponentAt(x,y)
    return findComponent(self,x,y)
end

-- @brief Lays out the child components in the layout bounds.
function GridLayout:layout()
    self.grid:layout(self.bounds)
    
    -- for each cell
    for _,cell in ipairs(self.cells) do
        local bounds0 = self.grid:getCellBounds(cell.x,cell.y)
        local bounds1 = self.grid:getCellBounds(
            cell.x + cell.xspan - 1,cell.y + cell.yspan - 1)
            
        cell.component:setBounds(bounds0.x,bounds0.y,
            bounds1:x1() - bounds0.x,bounds1:y1() - bounds0.y)
    end
end

return GridLayout