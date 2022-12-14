package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import cofh.thermal.expansion.inventory.container.machine.MachinePyrolyzerContainer;
import cofh.thermal.lib.tileentity.MachineTileBase;
import gay.heimskr.tradeperipheral.common.container.MobJuicerContainer;
import gay.heimskr.tradeperipheral.common.setup.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MobJuicerBlockEntity extends MachineTileBase {

	public MobJuicerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypes.MOB_JUICER.get(), pos, state);
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new MobJuicerContainer(i, level, worldPosition, inventory, player);
	}
}
