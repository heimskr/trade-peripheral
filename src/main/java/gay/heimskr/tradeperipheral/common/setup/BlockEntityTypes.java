package gay.heimskr.tradeperipheral.common.setup;

import cofh.thermal.expansion.block.entity.machine.MachinePyrolyzerTile;
import com.google.common.collect.Sets;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.lib.common.ThermalIDs.ID_MACHINE_PYROLYZER;

public class BlockEntityTypes {

	static void register() {}

	public static final RegistryObject<BlockEntityType<TraderBlockEntity>> TRADER = Registration.TILE_ENTITIES.register("trader", () ->
		new BlockEntityType<>(TraderBlockEntity::new, Sets.newHashSet(Blocks.TRADER.get()), null));

//	public static final RegistryObject<BlockEntityType<MobJuicerBlockEntity>> MOB_JUICER = Registration.TILE_ENTITIES.register("mob_juicer", () ->
//		new BlockEntityType<>(MobJuicerBlockEntity::new, Sets.newHashSet(Blocks.MOB_JUICER.get()), null));

	public static final RegistryObject<BlockEntityType<?>> MOB_JUICER = Registration.TILE_ENTITIES.register("mob_juicer", () -> BlockEntityType.Builder.of(MobJuicerBlockEntity::new, Blocks.MOB_JUICER.get()).build(null));

}
