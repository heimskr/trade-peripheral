package gay.heimskr.tradeperipheral.common.util;

//import appeng.api.storage.MEStorage;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.core.apis.TableHelper;
//import de.srendi.advancedperipherals.common.addons.appliedenergistics.AppEngApi;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.addons.refinedstorage.RefinedStorage;
import gay.heimskr.tradeperipheral.common.exception.ImpossibleException;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemUtil {
	public static <T extends ForgeRegistryEntry<T>> T getRegistryEntry(String name, IForgeRegistry<T> forgeRegistry) {
		ResourceLocation location;
		try {
			location = new ResourceLocation(name);
		} catch (ResourceLocationException ex) {
			location = null;
		}

		T value;
		if (location != null && forgeRegistry.containsKey(location) && (value = forgeRegistry.getValue(location)) != null)
			return value;

		return null;
	}

	public static ItemStack getItemStack(Map<?, ?> table, List<ItemStack> items) throws LuaException {
		if (table == null || table.isEmpty())
			return ItemStack.EMPTY;

		if (table.containsKey("fingerprint")) {
			ItemStack fingerprint = RefinedStorage.findMatchingFingerprint(TableHelper.getStringField(table, "fingerprint"), items);
			if (table.containsKey("count"))
				fingerprint.setCount(TableHelper.getIntField(table, "count"));
			return fingerprint;
		}

		if (!table.containsKey("name"))
			return ItemStack.EMPTY;

		String name = TableHelper.getStringField(table, "name");
		Item item = getRegistryEntry(name, ForgeRegistries.ITEMS);
		ItemStack stack = new ItemStack(item, 1);

		if (table.containsKey("count"))
			stack.setCount(TableHelper.getIntField(table, "count"));

		if (table.containsKey("nbt") || table.containsKey("json") || table.containsKey("tag"))
			stack.setTag(getTagRS(stack, table, items));

		return stack;
	}

	private static CompoundTag getTagRS(ItemStack stack, Map<?, ?> table, List<ItemStack> items) throws LuaException {
		CompoundTag nbt = NBTUtil.fromText(TableHelper.optStringField(table, "json", null));

		if (nbt == null) {
			nbt = NBTUtil.fromBinary(TableHelper.optStringField(table, "tag", null));
			if (nbt == null)
				return parseNBTHashRS(stack, table, items);
		}

		return nbt;
	}

	private static CompoundTag parseNBTHashRS(ItemStack stack, Map<?, ?> table, List<ItemStack> items) throws LuaException {
		String nbt = TableHelper.optStringField(table, "nbt", null);

		if (nbt == null || nbt.isEmpty())
			return null;

		CompoundTag tag = RefinedStorage.findMatchingTag(stack, nbt, items);

		return tag != null? tag : new CompoundTag();
	}

	//Gathers all items in handler and returns them
	public static List<ItemStack> getItemsFromItemHandler(IItemHandler handler) {
		List<ItemStack> items = new ArrayList<>(handler.getSlots());

		for (int slot = 0; slot < handler.getSlots(); ++slot)
			items.add(handler.getStackInSlot(slot).copy());

		return items;
	}

	private static boolean compareNBT(ItemStack one, ItemStack two) {
		if (one.getTag() == null)
			return two.getTag() == null;
		return one.getTag().equals(two.getTag());
	}

	public static boolean is(ItemStack one, ItemStack two) {
		return one == two || one.getCount() == two.getCount() && one.is(two.getItem()) && compareNBT(one, two);
	}

	public static boolean canExtract(IItemHandler handler, ItemStack checkStack) {
		return canExtract(handler, checkStack, true);
	}

	public static boolean canExtract(IItemHandler handler, ItemStack stack, boolean checkNBT) {
		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		final int slotCount = handler.getSlots();

		for (int slot = 0; slot < slotCount; ++slot) {
			ItemStack inSlot = handler.getStackInSlot(slot);
			if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot)))
				countRemaining -= inSlot.getCount();
		}

		return countRemaining <= 0;
	}

	public static boolean extract(IItemHandler handler, ItemStack stack) throws ImpossibleException {
		return extract(handler, stack, true);
	}

	public static boolean extract(IItemHandler handler, ItemStack stack, boolean checkNBT) throws ImpossibleException {
		if (!canExtract(handler, stack, checkNBT))
			return false;

		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		final int slotCount = handler.getSlots();

		for (int slot = 0; slot < slotCount; ++slot) {
			ItemStack inSlot = handler.getStackInSlot(slot);
			if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot))) {
				int toRemove = Math.min(countRemaining, inSlot.getCount());
				inSlot.shrink(toRemove);
				if ((countRemaining -= toRemove) <= 0)
					return true;
			}
		}

		throw new ImpossibleException("ItemStack should be removable from IItemHandler but isn't");
	}

	public static boolean canInsert(IItemHandler handler, ItemStack stack) {
		return canInsert(handler, stack, true);
	}

	public static boolean canInsert(IItemHandler handler, ItemStack stack, boolean checkNBT) {
		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		final int slotCount = handler.getSlots();

		for (int slot = 0; slot < slotCount; ++slot) {
			ItemStack inSlot = handler.getStackInSlot(slot);
			if (inSlot == null || inSlot.isEmpty() || inSlot.is(Items.AIR)) {
				if ((countRemaining -= stack.getMaxStackSize()) <= 0)
					return true;
			} else if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot)))
				if ((countRemaining -= inSlot.getMaxStackSize() - inSlot.getCount()) <= 0)
					return true;
		}

		return false;
	}

	public static boolean insert(IItemHandler handler, ItemStack stack) throws ImpossibleException {
		return insert(handler, stack, true);
	}

	public static boolean insert(IItemHandler handler, ItemStack stack, boolean checkNBT) throws ImpossibleException {
		if (!canInsert(handler, stack, checkNBT))
			return false;

		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		final int slotCount = handler.getSlots();

		for (int slot = 0; slot < slotCount; ++slot) {
			ItemStack inSlot = handler.getStackInSlot(slot);
			if (inSlot == null || inSlot.isEmpty() || inSlot.is(Items.AIR)) {
				ItemStack stackCopy = stack.copy();
				final int toAdd = Math.min(countRemaining, stack.getCount());
				stackCopy.setCount(toAdd);
				if (!handler.insertItem(slot, stackCopy, false).isEmpty())
					throw new ImpossibleException("insertItem returned a non-empty ItemStack");
				if ((countRemaining -= toAdd) <= 0)
					return true;
			} else if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot))) {
				int toAdd = Math.min(countRemaining, inSlot.getMaxStackSize() - inSlot.getCount());
				inSlot.grow(toAdd);
				if ((countRemaining -= toAdd) <= 0)
					return true;
			}
		}

		throw new ImpossibleException("ItemStack should be insertable into IItemHandler but isn't");
	}

	// I'm sorry for the horrible duplication but I'm not sure how I'd do this without something like C++'s delightful template system.

	public static boolean canExtract(List<ItemStack> stacks, ItemStack checkStack) {
		return canExtract(stacks, checkStack, true);
	}

	public static boolean canExtract(List<ItemStack> stacks, ItemStack stack, boolean checkNBT) {
		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		for (ItemStack inSlot: stacks)
			if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot)))
				countRemaining -= inSlot.getCount();

		return countRemaining <= 0;
	}

	public static boolean extract(List<ItemStack> stacks, ItemStack stack) throws ImpossibleException {
		return extract(stacks, stack, true);
	}

	public static boolean extract(List<ItemStack> stacks, ItemStack stack, boolean checkNBT) throws ImpossibleException {
		if (!canExtract(stacks, stack, checkNBT))
			return false;

		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		for (ItemStack inSlot: stacks) {
			if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot))) {
				int toRemove = Math.min(countRemaining, inSlot.getCount());
				inSlot.shrink(toRemove);
				if ((countRemaining -= toRemove) <= 0)
					return true;
			}
		}

		throw new ImpossibleException("ItemStack should be removable from List but isn't");
	}

	public static boolean canInsert(List<ItemStack> stacks, ItemStack stack) {
		return canInsert(stacks, stack, true);
	}

	public static boolean canInsert(List<ItemStack> stacks, ItemStack stack, boolean checkNBT) {
		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		for (ItemStack inSlot: stacks) {
			if (inSlot == null || inSlot.isEmpty() || inSlot.is(Items.AIR)) {
				if ((countRemaining -= stack.getMaxStackSize()) <= 0)
					return true;
			} else if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot)))
				if ((countRemaining -= inSlot.getMaxStackSize() - inSlot.getCount()) <= 0)
					return true;
		}

		return false;
	}

	public static boolean insert(List<ItemStack> stacks, ItemStack stack) throws ImpossibleException {
		return insert(stacks, stack, true);
	}

	public static boolean insert(List<ItemStack> stacks, ItemStack stack, boolean checkNBT) throws ImpossibleException {
		if (!canInsert(stacks, stack, checkNBT))
			return false;

		int countRemaining = stack.getCount();

		if (countRemaining == 0)
			return true;

		final int slotCount = stacks.size();

		for (int slot = 0; slot < slotCount; ++slot) {
			ItemStack inSlot = stacks.get(slot);
			if (inSlot == null || inSlot.isEmpty() || inSlot.is(Items.AIR)) {
				final int toAdd = Math.min(countRemaining, stack.getMaxStackSize());
				ItemStack stackCopy = stack.copy();
				stackCopy.setCount(toAdd);
				stacks.set(slot, stackCopy);
				if ((countRemaining -= toAdd) <= 0)
					return true;
			} else if (inSlot.sameItem(stack) && (!checkNBT || compareNBT(stack, inSlot))) {
				final int toAdd = Math.min(countRemaining, inSlot.getMaxStackSize() - inSlot.getCount());
				inSlot.grow(toAdd);
				if ((countRemaining -= toAdd) <= 0)
					return true;
			}
		}

		throw new ImpossibleException("ItemStack should be insertable into List but isn't");
	}
}
