package gay.heimskr.tradeperipheral;

import gay.heimskr.tradeperipheral.common.setup.Blocks;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.lib.common.ThermalIDs.*;
import static cofh.thermal.lib.common.ThermalIDs.ID_DYNAMO_GOURMAND;

@Mod(TradePeripheral.MOD_ID)
public class TradePeripheral {
	public static final String MOD_ID = "tradeperipheral";
	public static final Logger LOGGER = LogManager.getLogger("Trade Peripheral");
	public static final Random RANDOM = new Random();
	public static final CreativeModeTab TAB = new CreativeModeTab("tradeperipheraltab") {
		@Override
		@NotNull
		public ItemStack makeIcon() {
			return new ItemStack(Blocks.TRADER.get());
		}
	};

	public TradePeripheral() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		modBus.addListener(this::commonSetup);
		modBus.addListener(this::clientSetup);
		Registration.register();
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void debug(String message) {
		LOGGER.debug("[DEBUG] " + message);
	}

	public static void debug(String message, Level level) {
		LOGGER.log(level, "[DEBUG] " + message);
	}

	@SubscribeEvent
	public void commonSetup(FMLCommonSetupEvent event) {

	}

	public void clientSetup(FMLClientSetupEvent event) {
		registerRenderLayers();
	}

	@SubscribeEvent
	public void interModComms(InterModEnqueueEvent event) {

	}

	private void registerRenderLayers() {
		RenderType cutout = RenderType.cutout();
		ItemBlockRenderTypes.setRenderLayer(Blocks.MOB_JUICER.get(), cutout);
	}

}
