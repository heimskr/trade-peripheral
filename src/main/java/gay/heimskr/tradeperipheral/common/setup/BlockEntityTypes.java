package gay.heimskr.tradeperipheral.common.setup;

import com.google.common.collect.Sets;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypes {

	static void register() {}

	public static final RegistryObject<BlockEntityType<TraderBlockEntity>> TRADER = Registration.TILE_ENTITIES.register("trader", () ->
		new BlockEntityType<>(TraderBlockEntity::new, Sets.newHashSet(Blocks.TRADER.get()), null));

	public static final RegistryObject<BlockEntityType<MobJuicerBlockEntity>> MOB_JUICER = Registration.TILE_ENTITIES.register("mob_juicer", () ->
		new BlockEntityType<>(MobJuicerBlockEntity::new, Sets.newHashSet(Blocks.MOB_JUICER.get()), null));

}
