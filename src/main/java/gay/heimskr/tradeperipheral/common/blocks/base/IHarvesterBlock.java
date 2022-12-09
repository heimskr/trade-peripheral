package gay.heimskr.tradeperipheral.common.blocks.base;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface IHarvesterBlock {

    TagKey<Block> getHarvestTag();

    default TagKey<Block> getToolTag() {
        return BlockTags.MINEABLE_WITH_PICKAXE;
    }
}
