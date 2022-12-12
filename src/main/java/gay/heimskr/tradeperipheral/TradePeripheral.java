package gay.heimskr.tradeperipheral;

import gay.heimskr.tradeperipheral.common.configuration.APConfig;
import gay.heimskr.tradeperipheral.common.setup.Blocks;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Mod(TradePeripheral.MOD_ID)
public class TradePeripheral {
    public static final String MOD_ID = "tradeperipheral";
    public static final Logger LOGGER = LogManager.getLogger("Trade Peripheral");
//    public static final Random RANDOM = new Random();
    public static final CreativeModeTab TAB = new CreativeModeTab("tradeperipheraltab") {
        @Override
        @NotNull
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.TRADER.get());
        }
    };

    public TradePeripheral() {
        LOGGER.info("TradePeripheral says hello!");
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        APConfig.register(ModLoadingContext.get());

        modBus.addListener(this::commonSetup);
//        modBus.addListener(this::interModComms);
        modBus.addListener(this::clientSetup);
        Registration.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void debug(String message) {
        if (APConfig.GENERAL_CONFIG.enableDebugMode.get() || true)
            LOGGER.debug("[DEBUG] " + message);
    }

    public static void debug(String message, Level level) {
        if (APConfig.GENERAL_CONFIG.enableDebugMode.get() || true)
            LOGGER.log(level, "[DEBUG] " + message);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
//        event.enqueueWork(() -> {});
    }

    public void clientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void interModComms(InterModEnqueueEvent event) {
//        if (!curiosLoaded) return;
        //InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("glasses")
        //.size(1).icon(new ResourceLocation(MOD_ID, "textures/item/empty_glasses_slot.png")).build());
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("glasses").size(1).build());
    }

}
