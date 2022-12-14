package gay.heimskr.tradeperipheral.common.data;

import cofh.lib.data.ItemModelProviderCoFH;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.setup.Blocks;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.lib.common.ThermalIDs.*;

public class ModelProvider extends ItemModelProviderCoFH {
	public ModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, TradePeripheral.MOD_ID, existingFileHelper);
	}

	@Override
	public String getName() {
		return "Trade Peripheral: Item Models";
	}

	@Override
	protected void registerModels() {

		registerBlockItemModels();

        var reg = Registration.ITEMS;

		generated(reg.getSup("soul_sauce_bucket"));
	}

	private void registerBlockItemModels() {
        blockItem(Blocks.MOB_JUICER);
		blockItem(Blocks.TRADER);
	}

}
