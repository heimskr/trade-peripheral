package gay.heimskr.tradeperipheral.lib.peripherals;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BlockEntityIntegrationPeripheral<T extends BlockEntity> extends IntegrationPeripheral {

    protected final T blockEntity;

    public BlockEntityIntegrationPeripheral(BlockEntity entity) {
        super();
        this.blockEntity = (T) entity;
    }

    @Nullable
    @Override
    public Object getTarget() {
        return blockEntity;
    }
}
