package gay.heimskr.tradeperipheral.common.village;

import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TradePeripheral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTrades {

    @SubscribeEvent
    public static void registerWanderingTrade(WandererTradesEvent event) {

    }

    @SubscribeEvent
    public static void registerVillagerTrades(VillagerTradesEvent event) {

    }

}
