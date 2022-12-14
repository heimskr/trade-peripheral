package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import cofh.lib.api.StorageGroup;
import cofh.lib.api.control.IReconfigurable;
import cofh.lib.fluid.FluidStorageCoFH;
import cofh.lib.util.Constants;
import cofh.thermal.core.config.ThermalCoreConfig;
import cofh.thermal.expansion.inventory.container.machine.MachinePyrolyzerContainer;
import cofh.thermal.lib.tileentity.MachineTileBase;
import gay.heimskr.tradeperipheral.common.container.MobJuicerContainer;
import gay.heimskr.tradeperipheral.common.setup.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class MobJuicerBlockEntity extends MachineTileBase {

	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(Constants.TANK_LARGE);

	public MobJuicerBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityTypes.MOB_JUICER.get(), pos, state);

		tankInv.addTank(outputTank, StorageGroup.OUTPUT);
		addAugmentSlots(ThermalCoreConfig.machineAugments);

		initHandlers();
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new MobJuicerContainer(i, level, worldPosition, inventory, player);
	}

	@Override
	public boolean setSideConfig(Direction side, IReconfigurable.SideConfig config) {
		return side == Direction.UP? false : super.setSideConfig(side, config);
	}

	@Override
	public IReconfigurable.SideConfig getSideConfig(Direction side) {
		return side == Direction.UP? IReconfigurable.SideConfig.SIDE_NONE : super.getSideConfig(side);
	}
}
