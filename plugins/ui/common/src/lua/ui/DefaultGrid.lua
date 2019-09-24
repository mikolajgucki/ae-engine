-- @group UI
-- @brief The OpenGL implementation of a grid.
local DefaultGrid = ae.oo.class()
    
-- @brief Creates a DefaultGrid object.
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
-- @return A new DefaultGrid object. 
function DefaultGrid.new(columns,rows)
    local self = ae.oo.new(DefaultGrid)
    DefaultGrid.construct(self,columns,rows)
    
    return self
end

-- @brief Constructs a DefaultGrid object.
-- @param self The object. 
-- @param columns The widths of the columns.
-- @param rows The heights of the rows.
function DefaultGrid.construct(self,columns,rows)
    self.columns = columns
    self.rows = rows
end

-- @brief Calculate the fixed sizes.
-- @param totalSize The total size.
-- @param sizes The size table.
local function calcFixedSizes(totalSize,sizes)
    -- totals, fixed size
    local totalFixed = 0
    local totalRatio = 0
    for _,entry in ipairs(sizes) do
        if entry.fixed then
            totalFixed = totalFixed + entry.fixed
            entry.size = entry.fixed
        end
        if entry.ratio then
            totalRatio = totalRatio + entry.ratio
        end       
    end
    
    -- if don't fit
    if totalFixed > totalSize then
        ae.log.warning('Sizes do not fit')
        -- TODO Handle situation in which sizes do not fit.
    end
    
    -- ratio size
    for _,entry in ipairs(sizes) do
        if entry.ratio then
            entry.size = (totalSize - totalFixed) * entry.ratio / totalRatio
        end
    end    
end

-- @brief Calculates the coordinates.
-- @param translation The coordinate translation.
-- @param sizes The size table.
local function calcCoords(translation,sizes)
    local coord = translation
    local coords = {}
    
    -- for each size
    for index = 1,#sizes do 
        ae.itable.append(coords,coord)
        coord = coord + sizes[index].size
    end
    
    -- we keep the coordinates of the upper corners
    ae.itable.append(coords,coord)
    
    return coords
end

-- @brief Lays out the grid.
-- @param bounds The bounds in which to lay out the grid.
function DefaultGrid:layout(bounds)
    -- columns
    calcFixedSizes(bounds.width,self.columns)
    self.xcoords = calcCoords(bounds.x,self.columns)
    
    -- rows
    calcFixedSizes(bounds.height,self.rows)
    self.ycoords = calcCoords(bounds.y,self.rows)
end

-- @brief Gets bounds of a cell.
-- @param x The X coordinate of the cell.
-- @param y The Y coordinate of the cell.
-- @return The cell bounds.
function DefaultGrid:getCellBounds(x,y)
    local x0 = self.xcoords[x + 1]
    local y0 = self.ycoords[y + 1]
    
    local x1 = self.xcoords[x + 2]
    local y1 = self.ycoords[y + 2]
    
    return ui.Bounds.new(x0,y0,x1 - x0,y1 - y0)
end

return DefaultGrid