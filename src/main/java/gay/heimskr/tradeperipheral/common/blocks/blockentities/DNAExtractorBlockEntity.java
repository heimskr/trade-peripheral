package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import cofh.lib.client.sounds.ConditionalSoundInstance;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermal.core.config.ThermalCoreConfig;
import cofh.thermal.expansion.inventory.container.machine.MachinePulverizerContainer;
import cofh.thermal.lib.tileentity.MachineTileBase;
import gay.heimskr.tradeperipheral.common.util.machine.DNAExtractorRecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static cofh.lib.api.StorageGroup.*;
import static cofh.lib.api.StorageGroup.INTERNAL;
import static cofh.thermal.expansion.init.TExpSounds.SOUND_MACHINE_PULVERIZER;
import static cofh.thermal.expansion.init.TExpTileEntities.MACHINE_PULVERIZER_TILE;

public class DNAExtractorBlockEntity extends MachineTileBase {

	protected ItemStorageCoFH inputSlot = new ItemStorageCoFH(item -> filter.valid(item) && DNAExtractorRecipeManager.instance().validRecipe(item));

	public DNAExtractorBlockEntity(BlockPos pos, BlockState state) {

		super(MACHINE_PULVERIZER_TILE.get(), pos, state);

		inventory.addSlot(inputSlot, INPUT);
		inventory.addSlots(OUTPUT, 4);
		inventory.addSlot(chargeSlot, INTERNAL);

		addAugmentSlots(ThermalCoreConfig.machineAugments);
		initHandlers();
	}

	@Override
	protected int getBaseProcessTick() {

		return DNAExtractorRecipeManager.instance().getBasePower();
	}

	@Override
	protected boolean cacheRecipe() {

		curRecipe = DNAExtractorRecipeManager.instance().getRecipe(this);
		if (curRecipe != null)
			itemInputCounts = curRecipe.getInputItemCounts(this);
		return curRecipe != null;
	}

	@Override
	protected void resolveInputs() {
		// Input Items
		inputSlot.modify(-itemInputCounts.get(0));
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return new MachinePulverizerContainer(i, level, worldPosition, inventory, player);
	}

	@Override
	protected Object getSound() {
		return new ConditionalSoundInstance(SOUND_MACHINE_PULVERIZER.get(), SoundSource.AMBIENT, this, () -> !remove && isActive);
	}

	@Override
	protected boolean validateInputs() {
		return cacheRecipe() && inputSlot.getCount() >= itemInputCounts.get(0);
	}
}

