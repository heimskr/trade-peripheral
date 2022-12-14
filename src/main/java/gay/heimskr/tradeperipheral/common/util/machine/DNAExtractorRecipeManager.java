package gay.heimskr.tradeperipheral.common.util.machine;

import cofh.lib.util.crafting.ComparableItemStack;
import cofh.lib.util.crafting.ComparableItemStackNBT;
import cofh.thermal.lib.util.managers.SingleItemRecipeManager;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import cofh.thermal.lib.util.recipes.internal.*;
import gay.heimskr.tradeperipheral.common.setup.RecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class DNAExtractorRecipeManager extends SingleItemRecipeManager.Catalyzed {

	private static final DNAExtractorRecipeManager INSTANCE = new DNAExtractorRecipeManager();
	protected static final int DEFAULT_ENERGY = 4000;

	public static DNAExtractorRecipeManager instance() {
		return INSTANCE;
	}

	private DNAExtractorRecipeManager() {
		super(DEFAULT_ENERGY, 4, 0);
	}

	@Override
	protected IMachineRecipe addRecipe(int energy, float experience, List<ItemStack> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<Float> chance, List<FluidStack> outputFluids, BaseMachineRecipe.RecipeType type) {
		if (inputItems.isEmpty() || outputItems.isEmpty() && outputFluids.isEmpty() || outputItems.size() > maxOutputItems || outputFluids.size() > maxOutputFluids || energy <= 0)
			return null;

		ItemStack input = inputItems.get(0);
		if (input.isEmpty())
			return null;

		for (ItemStack stack: outputItems)
			if (stack.isEmpty())
				return null;

		for (FluidStack stack: outputFluids)
			if (stack.isEmpty())
				return null;

		energy = (int) (energy * getDefaultScale());

		IMachineRecipe recipe = new InternalDNAExtractorRecipe(energy, experience, inputItems, inputFluids, outputItems, chance, outputFluids);
		recipeMap.put(new ComparableItemStackNBT(input), recipe);

		return recipe;
	}

	@Override
	public void refresh(RecipeManager recipeManager) {
		clear();
		var recipes = recipeManager.byType(RecipeTypes.RECIPE_DNA_EXTRACTOR);
		for (var entry: recipes.entrySet())
			addRecipe((ThermalRecipe) entry.getValue());
	}

	protected static class InternalDNAExtractorRecipe extends CatalyzedMachineRecipe {
		public InternalDNAExtractorRecipe(int energy, float experience, @Nullable List<ItemStack> inputItems, @Nullable List<FluidStack> inputFluids, @Nullable List<ItemStack> outputItems, @Nullable List<Float> chance, @Nullable List<FluidStack> outputFluids) {
			super(energy, experience, inputItems, inputFluids, outputItems, chance, outputFluids);
		}

		@Override
		public IRecipeCatalyst getCatalyst(ItemStack input) {
			return instance().getCatalyst(input);
		}

	}
}
