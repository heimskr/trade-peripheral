package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.Capabilities;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral.TraderPeripheral;
import gay.heimskr.tradeperipheral.common.blocks.base.IInventoryBlock;
import gay.heimskr.tradeperipheral.common.blocks.base.PeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.container.TraderContainer;
import gay.heimskr.tradeperipheral.common.setup.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

    @Override
    public int[] getSlotsForFace(@NotNull Direction side) {
        Direction facing = null;

        var cap = getCapability(Capabilities.CAPABILITY_PERIPHERAL);
        if (cap.isPresent()) {
            IPeripheral periph = cap.orElse(null);
            if (periph != null)
                facing = ((TraderPeripheral) periph).getPeripheralOwner().getFacing();
        }

        if (facing != null) {
            switch (facing) {
                case NORTH: break;
                case EAST:  side = side.getCounterClockWise(); break;
                case SOUTH: side = side.getOpposite(); break;
                case WEST:  side = side.getClockWise(); break;
                case DOWN:  side = side.getCounterClockWise(Direction.Axis.X); break;
                case UP:    side = side.getClockWise(Direction.Axis.X); break;
            }
        }

        switch (side) {
            case NORTH: return new int[] {0};
            case EAST:  return new int[] {1};
            case WEST:  return new int[] {2};
            case SOUTH: return new int[] {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
            default:
                return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        }
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.tradeperipheral.trader");
    }
}
