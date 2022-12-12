package gay.heimskr.tradeperipheral.common.data;

import dan200.computercraft.shared.Registry;
import gay.heimskr.tradeperipheral.common.setup.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipesProvider extends RecipeProvider implements IConditionBuilder {
	public RecipesProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(Blocks.TRADER.get())
			.define('E', Items.EMERALD)
			.define('I', net.minecraft.world.level.block.Blocks.IRON_BLOCK)
			.pattern("EEE")
			.pattern("EIE")
			.pattern("EEE")
			.unlockedBy("has_item", has(net.minecraft.world.level.block.Blocks.IRON_BLOCK))
			.save(consumer);
	}

	public static class NBTIngredient extends net.minecraftforge.common.crafting.NBTIngredient {
		public NBTIngredient(ItemStack stack) {
			super(stack);
		}
	}
}
