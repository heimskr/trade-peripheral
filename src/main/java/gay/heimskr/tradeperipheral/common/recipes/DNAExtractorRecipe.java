package gay.heimskr.tradeperipheral.common.recipes;

import cofh.lib.fluid.FluidIngredient;
import cofh.thermal.core.ThermalCore;
import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.core.util.managers.machine.PulverizerRecipeManager;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.setup.RecipeTypes;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import gay.heimskr.tradeperipheral.common.util.machine.DNAExtractorRecipeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class DNAExtractorRecipe extends ThermalRecipe {

	public DNAExtractorRecipe(ResourceLocation recipeId, int energy, float experience, List<Ingredient> inputItems, List<FluidIngredient> inputFluids, List<ItemStack> outputItems, List<Float> outputItemChances, List<FluidStack> outputFluids) {
		super(recipeId, energy, experience, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);

		if (this.energy <= 0) {
			int defaultEnergy = DNAExtractorRecipeManager.instance().getDefaultEnergy();
			TradePeripheral.LOGGER.warn("Energy value for " + recipeId + " was out of allowable range and has been set to a default value of " + defaultEnergy + ".");
			this.energy = defaultEnergy;
		}
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return Registration.RECIPE_SERIALIZERS.get(RecipeTypes.ID_RECIPE_DNA_EXTRACTOR);
	}

	@Nonnull
	@Override
	public RecipeType<?> getType() {
		return RecipeTypes.RECIPE_DNA_EXTRACTOR;
	}

}
