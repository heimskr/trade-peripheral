package gay.heimskr.tradeperipheral.common.setup;

import gay.heimskr.tradeperipheral.common.container.MobJuicerContainer;
import gay.heimskr.tradeperipheral.common.container.TraderContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

public class ContainerTypes {

    public static void register() {}

    public static final RegistryObject<MenuType<TraderContainer>> TRADER_CONTAINER = Registration.CONTAINER_TYPES.register("trader_container", () -> IForgeMenuType.create((windowId, inv, data) ->
        new TraderContainer(windowId, inv, data.readBlockPos(), inv.player.getCommandSenderWorld())));

    public static final RegistryObject<MenuType<MobJuicerContainer>> MOB_JUICER_CONTAINER = Registration.CONTAINER_TYPES.register("mob_juicer_container", () -> IForgeMenuType.create((windowId, inv, data) ->
        new MobJuicerContainer(windowId, inv.player.getCommandSenderWorld(), data.readBlockPos(), inv, inv.player)));
}
