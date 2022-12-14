package gay.heimskr.tradeperipheral.common.setup;

import cofh.thermal.lib.util.recipes.MachineRecipeSerializer;
import gay.heimskr.tradeperipheral.common.recipes.DNAExtractorRecipe;
import gay.heimskr.tradeperipheral.common.util.machine.DNAExtractorRecipeManager;

public class RecipeSerializers {
	public static void register() {
		Registration.RECIPE_SERIALIZERS.register(RecipeTypes.ID_RECIPE_DNA_EXTRACTOR, () -> new MachineRecipeSerializer<>(DNAExtractorRecipe::new, DNAExtractorRecipeManager.instance().getDefaultEnergy()));
	}
}
