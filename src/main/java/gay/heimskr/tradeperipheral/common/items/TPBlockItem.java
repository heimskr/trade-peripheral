package gay.heimskr.tradeperipheral.common.items;

import gay.heimskr.tradeperipheral.common.items.base.BaseBlockItem;
import net.minecraft.world.level.block.Block;

public class TPBlockItem extends BaseBlockItem {

//    private final Supplier<Boolean> enabledSup;

    public TPBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties);
    }

    public TPBlockItem(Block blockIn) {
        super(blockIn);
    }

//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
