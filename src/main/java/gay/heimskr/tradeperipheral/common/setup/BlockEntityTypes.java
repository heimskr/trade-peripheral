package gay.heimskr.tradeperipheral.common.setup;

import com.google.common.collect.Sets;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypes {

    static void register() {
    }

    public static final RegistryObject<BlockEntityType<TraderEntity>> TRADER = Registration.TILE_ENTITIES.register("trader", () -> new BlockEntityType<>(TraderEntity::new, Sets.newHashSet(Blocks.TRADER.get()), null));

}
