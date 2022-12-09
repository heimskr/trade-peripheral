package gay.heimskr.tradeperipheral.common.setup;

import gay.heimskr.tradeperipheral.common.blocks.base.APBlockEntityBlock;
import gay.heimskr.tradeperipheral.common.configuration.APConfig;
import gay.heimskr.tradeperipheral.common.items.TPBlockItem;
import gay.heimskr.tradeperipheral.common.items.base.BaseBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Blocks {

	static void register() {
	}

	public static final RegistryObject<Block> TRADER = register("trader", () ->
		new APBlockEntityBlock<>(BlockEntityTypes.TRADER, false), () ->
			new BaseBlockItem(Blocks.TRADER.get()));

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
