package gay.heimskr.tradeperipheral.common.setup;

import cofh.lib.util.recipes.SerializableRecipeType;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.recipes.DNAExtractorRecipe;
import net.minecraft.resources.ResourceLocation;

public class RecipeTypes {
	public static final ResourceLocation ID_RECIPE_DNA_EXTRACTOR = new ResourceLocation(TradePeripheral.MOD_ID, "dna_extractor");
	public static final SerializableRecipeType<DNAExtractorRecipe> RECIPE_DNA_EXTRACTOR = new SerializableRecipeType<>(ID_RECIPE_DNA_EXTRACTOR);

	public static void register() {
		RECIPE_DNA_EXTRACTOR.register();
	}
}
