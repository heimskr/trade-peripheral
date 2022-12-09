package gay.heimskr.tradeperipheral.common.setup;

import gay.heimskr.tradeperipheral.common.container.TraderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public class ContainerTypes {

    public static final RegistryObject<MenuType<TraderContainer>> TRADER_CONTAINER = Registration.CONTAINER_TYPES.register("trader_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level level = inv.player.getCommandSenderWorld();
        return new TraderContainer(windowId, inv, pos, level);
    }));

    public static void register() {

    }
}
