package gay.heimskr.tradeperipheral.common.container;

import cofh.core.inventory.container.TileContainer;
import cofh.lib.inventory.container.slot.SlotCoFH;
import cofh.lib.inventory.container.slot.SlotRemoveOnly;
import cofh.lib.inventory.wrapper.InvWrapperCoFH;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.MobJuicerBlockEntity;
import gay.heimskr.tradeperipheral.common.setup.ContainerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public class MobJuicerContainer extends TileContainer {
	private final MobJuicerBlockEntity blockEntity;

	public MobJuicerContainer(int windowId, Level level, BlockPos pos, Inventory inventory, Player player) {
		super(ContainerTypes.MOB_JUICER_CONTAINER.get(), windowId, level, pos, inventory, player);

		this.blockEntity = (MobJuicerBlockEntity) level.getBlockEntity(pos);
		InvWrapperCoFH tileInv = new InvWrapperCoFH(this.blockEntity.getItemInv());

		addSlot(new SlotCoFH(tileInv, 0, 44, 26));

		addSlot(new SlotRemoveOnly(tileInv, 1, 107, 26));
		addSlot(new SlotRemoveOnly(tileInv, 2, 125, 26));
		addSlot(new SlotRemoveOnly(tileInv, 3, 107, 44));
		addSlot(new SlotRemoveOnly(tileInv, 4, 125, 44));

		addSlot(new SlotCoFH(tileInv, 5, 8, 53));

//		bindAugmentSlots(tileInv, 6, this.blockEntity.augSize());
		bindPlayerInventory(inventory);
	}
}
