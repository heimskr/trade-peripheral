package gay.heimskr.tradeperipheral.common.setup;

import cofh.core.block.TileBlockActive4Way;
import cofh.thermal.core.ThermalCore;
import cofh.thermal.core.config.ThermalCoreConfig;
import cofh.thermal.core.util.RegistrationHelper;
import cofh.thermal.lib.item.BlockItemAugmentable;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.blocks.base.TPBlockEntityBlock;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.MobJuicerBlockEntity;
import gay.heimskr.tradeperipheral.common.items.base.BaseBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static cofh.lib.util.constants.BlockStatePropertiesCoFH.ACTIVE;
import static cofh.lib.util.helpers.BlockHelper.lightValue;
import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.lib.common.ThermalAugmentRules.MACHINE_VALIDATOR;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public class Blocks {

	public static void register() {}

	public static final RegistryObject<Block> TRADER = register("trader", () ->
		new TPBlockEntityBlock<>(BlockEntityTypes.TRADER, false), () ->
			new BaseBlockItem(Blocks.TRADER.get()));

	public static final RegistryObject<Block> MOB_JUICER = registerMachine("mob_juicer", MobJuicerBlockEntity.class, () -> BlockEntityTypes.MOB_JUICER.get());

	public static RegistryObject<Block> registerMachine(String name, Class cls, Supplier<BlockEntityType<?>> entityTypeSupplier) {
		var block = Registration.BLOCKS.register(name, () -> (Block) new TileBlockActive4Way(of(Material.METAL).sound(SoundType.NETHERITE_BLOCK).strength(2.0F).lightLevel(lightValue(ACTIVE, 8)), cls, entityTypeSupplier));
		Registration.ITEMS.register(name, (Supplier<Item>) () -> new BlockItemAugmentable(block.get(), new Item.Properties().tab(TradePeripheral.TAB).rarity(Rarity.COMMON)).setNumSlots(() -> ThermalCoreConfig.machineAugments).setAugValidator(MACHINE_VALIDATOR).setShowInGroups(() -> true).setModId(TradePeripheral.MOD_ID));
		return block;
	}

	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
		return Registration.BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, Supplier<BlockItem> blockItem) {
		RegistryObject<T> registryObject = registerNoItem(name, block);
		Registration.ITEMS.register(name, blockItem);
		return registryObject;
	}

	public static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
}
