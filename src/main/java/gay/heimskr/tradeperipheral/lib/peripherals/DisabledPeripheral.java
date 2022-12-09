package gay.heimskr.tradeperipheral.lib.peripherals;

import dan200.computercraft.api.pocket.IPocketAccess;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.PocketPeripheralOwner;

public class DisabledPeripheral extends BasePeripheral<PocketPeripheralOwner> {
    public static final DisabledPeripheral INSTANCE = new DisabledPeripheral("disabledPeripheral", null);

    private DisabledPeripheral(String type, IPocketAccess access) {
        super(type, new PocketPeripheralOwner(access));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
