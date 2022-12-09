package gay.heimskr.tradeperipheral.common.setup;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.addons.computercraft.integrations.IntegrationPeripheralProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = TradePeripheral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCRegistration {

    public static IntegrationPeripheralProvider integrationPeripheralProvider;

    public static void register() {
        IntegrationPeripheralProvider.load();
        integrationPeripheralProvider = new IntegrationPeripheralProvider();
        ComputerCraftAPI.registerPeripheralProvider(integrationPeripheralProvider);
    }

    public static class ID {

    }
}
