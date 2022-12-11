package gay.heimskr.tradeperipheral.common.container;

import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.container.base.BaseContainer;
import gay.heimskr.tradeperipheral.common.container.base.SlotCondition;
import gay.heimskr.tradeperipheral.common.container.base.SlotInputHandler;
import gay.heimskr.tradeperipheral.common.setup.ContainerTypes;
import gay.heimskr.tradeperipheral.common.setup.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import com.lothrazar.villagertools.ModRegistry;

public class TraderContainer extends BaseContainer {

    public TraderContainer(int id, Inventory inventory, BlockPos pos, Level level) {
        super(ContainerTypes.TRADER_CONTAINER.get(), id, inventory, pos, level);
        layoutPlayerInventorySlots(7, 84);
        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                addSlot(new SlotInputHandler(handler, 0, 79 - 9, 29, new SlotCondition().setNeededItem(net.minecraft.world.item.Items.EMERALD)));
                addSlot(new SlotInputHandler(handler, 1, 79 + 9, 29, new SlotCondition().setNeededItem(ModRegistry.RESTOCK.get())));
            });
        }
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= 36) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index <= 35) {
                if (itemstack1.getItem().equals(net.minecraft.world.item.Items.EMERALD)) {
                    if (!this.moveItemStackTo(itemstack1, 36, 37, true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem().equals(ModRegistry.RESTOCK.get())) {
                    if (!this.moveItemStackTo(itemstack1, 37, 38, true)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

}
