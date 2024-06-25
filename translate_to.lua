-- Brought to you by Chien-Rouge aka RedDog,csa or TW2

local script_name="Auto translate"
local script_description="Translate with LibreTranslate"
local script_author="Chien-Rouge"
local script_version="1.0" --To make sure you and your users know which version is newest

-- Modify these values before use the script
local ast_path = "G:\\Translate\\Test\\"
local result_file = "G:\\Translate\\Test\\result.txt"

local function sleep(s)
    local ntime = os.clock() + s/10
    repeat until os.clock() > ntime
end

local register_name = "Translate selected lines"
local readable_name = "LibreTranslate and AST help Aegisub"

local function translate(subs, sel)
    
	for i = 1, #sel do
        
		local line = subs[sel[i]]

        -- We are waiting for a response typed as 'from>to' or '?to':
        -- - ISO639>ISO639 (example: ja>en)
        -- - ?ISO639 (example: ?en)
        -- Make sure libretranslate is launched and AST works
        -- and in same folder that this script
        -- In your template write one of the following:
        -- - ISO639>ISO639
        -- - ?ISO639
        os.execute("java -jar " .. ast_path .. "ast.jar "
        .. "\"" .. line.effect .. "\" "
        .. "\"" .. line.text .. "\" "
        .. "\"" .. result_file .. "\"")
        
        sleep(2)

        local f = assert(io.open(result_file, "r"))
        local t = f:read("*all")
        f:close()
        
        local newline = line
        
        newline.comment = false
        newline.text = t

        subs.append(newline)
	end
	
    aegisub.set_undo_point(register_name)
	
end

aegisub.register_macro(register_name, readable_name, translate)
