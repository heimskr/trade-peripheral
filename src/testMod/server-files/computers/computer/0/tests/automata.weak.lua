local p = peripheral.find("weakAutomata")
test.assert(p, "Not automata found")
local firstLookResult = p.lookAtBlock()
test.eq("minecraft:cobblestone", firstLookResult.name)
test.neq(0, #(firstLookResult.tags), "Cobblestone has empty tags? Really")
local dig, digErr = p.digBlock()
test.eq(nil, digErr, "Dig finished with error")
test.eq(true, dig, "Dig should be successful")
local scanResult, scanErr = p.scanItems()
test.eq(nil, scanErr, "Errors on scan")
test.eq(1, #scanResult, "Scan result bigger then 1?")
test.eq("[Cobblestone]", scanResult[1].name, "Scan found not cobblestone")
local suckResult, suckError = p.collectItems(1)
test.eq(nil, suckError, "Suck finished with error")
test.eq(true, suckResult, "Suck should be successful")
test.eq({ count = 1, name = "minecraft:cobblestone" }, turtle.getItemDetail(3), "Problem with suck result details?")
local secondLookResult = p.lookAtBlock()
test.eq("minecraft:stone_bricks", secondLookResult.name, "Second look at block with problems")
test.neq(0, #(secondLookResult.tags), "stone bricks has empty tags? Really")
turtle.select(2)
local useResult, useError = p.useOnBlock()
test.eq("PASS", useError, "Use on block finished with error")
test.eq(true, useResult, "Use on block should be successful")
local lookAtEntityResult = p.lookAtEntity()
test.eq("Cow", lookAtEntityResult.name, "Problem with entity")
