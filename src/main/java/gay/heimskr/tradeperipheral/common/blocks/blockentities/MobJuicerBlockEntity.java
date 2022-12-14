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
import gay.heimskr.tradeperipheral.common.setup.FluidTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;


public class MobJuicerBlockEntity extends MachineTileBase {

	// Lol.
	protected int ticks = 0;

	public static final int ENERGY_PER_ACTION = 5000;

	protected FluidStorageCoFH outputTank = new FluidStorageCoFH(Constants.TANK_LARGE).setEmptyFluid(() -> new FluidStack(FluidTypes.SOUL_SAUCE_FLUID.get(), 0));

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

	@Override
	public void tickServer() {
		if (ENERGY_PER_ACTION <= energyStorage.getEnergyStored() && ++ticks % (5 * 20) == 0)
			activate();
	}

	public void activate() {
		var targets = getLevel().getEntities((Entity) null, new AABB(getBlockPos()).inflate(0., 2., 0.), entity -> (entity instanceof LivingEntity) && !(entity instanceof Player));

		if (targets.isEmpty() || energyStorage.getEnergyStored() < ENERGY_PER_ACTION)
			return;

		var target = (LivingEntity) targets.get(0);
		outputTank.modify((int) (target.getMaxHealth() * 100));
		target.hurt(DamageSource.STARVE, Float.MAX_VALUE);
		energyStorage.modify(-ENERGY_PER_ACTION);
		markDirtyFast();
	}
}
