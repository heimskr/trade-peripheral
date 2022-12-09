package gay.heimskr.tradeperipheral.common.addons.computercraft.owner;

import gay.heimskr.tradeperipheral.common.configuration.APConfig;
import gay.heimskr.tradeperipheral.lib.peripherals.IPeripheralTileEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;

public class TileEntityFuelAbility<T extends BlockEntity & IPeripheralTileEntity> extends FuelAbility<BlockEntityPeripheralOwner<T>> {
    private static final int ENERGY_TO_FUEL_RATE = 575;

    public TileEntityFuelAbility(@NotNull BlockEntityPeripheralOwner<T> owner) {
        super(owner);
    }

    @Override
    protected boolean consumeFuel(int count) {
        return owner.tileEntity.getCapability(CapabilityEnergy.ENERGY).map(storage -> {
//            int energyCount = count * APConfig.METAPHYSICS_CONFIG.energyToFuelRate.get();
            int energyCount = count * ENERGY_TO_FUEL_RATE;
            int extractedCount = storage.extractEnergy(energyCount, true);
            if (extractedCount == energyCount) {
                storage.extractEnergy(energyCount, false);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    protected int getMaxFuelConsumptionRate() {
        return DEFAULT_FUEL_CONSUMING_RATE;
    }

    @Override
    public boolean isFuelConsumptionDisable() {
        return false;
    }

    @Override
    public int getFuelCount() {
        return owner.tileEntity.getCapability(CapabilityEnergy.ENERGY).map(storage -> storage.getEnergyStored() / ENERGY_TO_FUEL_RATE).orElse(0);
    }

    @Override
    public int getFuelMaxCount() {
        return owner.tileEntity.getCapability(CapabilityEnergy.ENERGY).map(storage -> storage.getMaxEnergyStored() / ENERGY_TO_FUEL_RATE).orElse(0);
    }

    @Override
    public void addFuel(int count) {
        owner.tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(storage -> {
//            int energyCount = count * APConfig.METAPHYSICS_CONFIG.energyToFuelRate.get();
            int energyCount = count * ENERGY_TO_FUEL_RATE;
            storage.receiveEnergy(energyCount, false);
        });
    }
}
