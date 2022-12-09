package gay.heimskr.tradeperipheral.common.blocks.blockentities;

import gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral.TraderPeripheral;
import gay.heimskr.tradeperipheral.common.blocks.base.PoweredPeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.setup.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TraderEntity extends PoweredPeripheralBlockEntity<TraderPeripheral> {

    public TraderEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.TRADER.get(), pos, state);
    }

    @Override
    protected int getMaxEnergyStored() {
        return 100_000_000;
    }

    @NotNull
    @Override
    protected TraderPeripheral createPeripheral() {
        return new TraderPeripheral(this);
    }

}
