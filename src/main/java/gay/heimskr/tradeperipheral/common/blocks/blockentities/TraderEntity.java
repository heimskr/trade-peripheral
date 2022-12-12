package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral.TraderPeripheral;
import gay.heimskr.tradeperipheral.common.blocks.base.IInventoryBlock;
import gay.heimskr.tradeperipheral.common.blocks.base.PeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.blocks.base.PoweredPeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.container.TraderContainer;
import gay.heimskr.tradeperipheral.common.setup.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TraderEntity extends PeripheralBlockEntity<TraderPeripheral> implements IInventoryBlock<TraderContainer> {

    public TraderEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.TRADER.get(), pos, state);
    }

    @NotNull
    @Override
    protected TraderPeripheral createPeripheral() {
        return new TraderPeripheral(this);
    }

    @Override
    public TraderContainer createContainer(int id, Inventory playerInventory, BlockPos pos, Level world) {
        return new TraderContainer(id, playerInventory, pos, world);
    }

    @Override
    public int getInvSize() {
        return 27;
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.tradeperipheral.trader");
    }
}
