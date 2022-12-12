package gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral;

import com.lothrazar.villagertools.ModRegistry;
import dan200.computercraft.api.lua.*;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.BlockEntityPeripheralOwner;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.IPeripheralOwner;
import gay.heimskr.tradeperipheral.common.blocks.base.PeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.TraderEntity;
import gay.heimskr.tradeperipheral.common.exception.ImpossibleException;
import gay.heimskr.tradeperipheral.common.util.ItemUtil;
import gay.heimskr.tradeperipheral.common.util.LuaConverter;
import gay.heimskr.tradeperipheral.lib.peripherals.BasePeripheral;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

import org.apache.logging.log4j.Logger;

public class TraderPeripheral extends BasePeripheral<IPeripheralOwner> {

	public static final String TYPE = "trader";
	private static final Logger LOGGER = TradePeripheral.LOGGER;

	protected TraderPeripheral(IPeripheralOwner owner) {
		super(TYPE, owner);
	}

	public TraderPeripheral(PeripheralBlockEntity<?> tileEntity) {
		this(new BlockEntityPeripheralOwner<>(tileEntity).attachFuel());
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public static class Trade {
		ItemStack costA;
		ItemStack costB;
		ItemStack result;
		int uses;
		int max;

		public Trade(MerchantOffer offer) {
			costA = offer.getCostA();
			costB = offer.getCostB();
			result = offer.getResult();
			uses = offer.getUses();
			max = offer.getMaxUses();
		}

		private Object encodeItemStack(ItemStack stack) {
			return LuaConverter.stackToObject(stack);
		}

		public List<Object> toLua() {
			List<Object> costs = Arrays.asList(encodeItemStack(costA));
			if (!(costB.getItem() instanceof AirItem))
				costs.add(encodeItemStack(costB));
			return Arrays.asList(costs, uses, max);
		}
	}

	private final List<Villager> getVillagers() {
		var villagers = new ArrayList<Villager>();

		getLevel().getEntities((Entity) null, new AABB(owner.getPos()).inflate(0., 2., 0.), Villager.class::isInstance).forEach(entity -> {
			if (entity instanceof Villager)
				villagers.add((Villager) entity);
		});

		return villagers;
	}

	@LuaFunction(mainThread = true)
	public final MethodResult getTrades() {
		var villagers = getVillagers();

		if (villagers.size() == 0)
			return MethodResult.of("no_villagers");

		if (villagers.size() == 1)
			return MethodResult.of(villagers.get(0).getOffers().stream().map(offer -> new Trade(offer).toLua()).toList());

		return MethodResult.of("multiple_villagers");
	}

	@LuaFunction(mainThread = true)
	public final MethodResult restock() {
		TraderEntity entity = (TraderEntity) getLevel().getBlockEntity(getPos());

		ItemStack stack = entity.getItem(1);
		int restocks = 0;
		int initialSize = stack.getCount();

		if (!stack.isEmpty() && stack.getItem().equals(ModRegistry.RESTOCK.get())) {
			for (Villager villager: getVillagers()) {
				boolean restocked = false;

				for (MerchantOffer offer: villager.getOffers()) {
					if (0 < offer.getUses()) {
						offer.resetUses();
						restocked = true;
					}
				}

				if (restocked) {
					++restocks;
					stack.shrink(1);
					if (stack.isEmpty())
						break;
				}
			}
		}

		return MethodResult.of(Arrays.asList(restocks, initialSize - restocks));
	}

	@LuaFunction(mainThread = true)
	public final MethodResult doTrade(Optional<Map<?, ?>> costA, Optional<Map<?, ?>> costB, Optional<Map<?, ?>> result, int count) throws LuaException {
		if ((costA.isEmpty() && result.isEmpty()) || count < 1)
			return MethodResult.of("invalid_args");

		TraderEntity entity = (TraderEntity) getLevel().getBlockEntity(getPos());
		IItemHandler handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().orElse(null);
		var fromHandler = ItemUtil.getItemsFromItemHandler(handler);
		ItemStack stackA = costA.isEmpty()? null : ItemUtil.getItemStack(costA.get(), fromHandler);
		ItemStack stackB = costB.isEmpty()? null : ItemUtil.getItemStack(costB.get(), fromHandler);
		ItemStack stackResult = result.isEmpty()? null : ItemUtil.getItemStack(result.get(), fromHandler);

		if (stackA == null) TradePeripheral.debug("stackA: null"); else TradePeripheral.debug("stackA: " + stackA.toString());
		if (stackB == null) TradePeripheral.debug("stackB: null"); else TradePeripheral.debug("stackB: " + stackB.toString());
		if (stackResult == null) TradePeripheral.debug("stackResult: null"); else TradePeripheral.debug("stackResult: " + stackResult.toString());

		MerchantOffer foundOffer = null;

		for (Villager villager: getVillagers()) {
			for (MerchantOffer offer: villager.getOffers()) {

				if ((costA.isEmpty() || ItemUtil.is(offer.getCostA(), stackA)) && (costB.isEmpty() || ItemUtil.is(offer.getCostB(), stackB)) && (result.isEmpty() || ItemUtil.is(offer.getResult(), stackResult))) {
					if (foundOffer != null)
						return MethodResult.of("multiple_offers");
					foundOffer = offer;
				} else {
//					TradePeripheral.debug("costA: " + offer.getCostA());
//					TradePeripheral.debug("costB: " + offer.getCostB());
//					TradePeripheral.debug("result: " + offer.getResult());
//					TradePeripheral.debug((costA.isEmpty() || ItemUtil.is(offer.getCostA(), stackA)) + " && " + (costB.isEmpty() || ItemUtil.is(offer.getCostB(), stackB)) + " && " + (result.isEmpty() || ItemUtil.is(offer.getResult(), stackResult)));
				}
			}
		}

		if (foundOffer == null)
			return MethodResult.of("no_offer");

		for (int i = 0; i < count; ++i) {
			if (foundOffer.getMaxUses() <= foundOffer.getUses())
				return MethodResult.of("exhausted");

			List<ItemStack> itemList = new ArrayList<>();

			for (ItemStack toCopy : ItemUtil.getItemsFromItemHandler(handler))
				itemList.add(toCopy.copy());

			if (!ItemUtil.canExtract(itemList, foundOffer.getCostA()) || !ItemUtil.canExtract(itemList, foundOffer.getCostB()))
				return MethodResult.of("cannot_afford");

			try {
				ItemUtil.extract(itemList, foundOffer.getCostA());
				ItemUtil.extract(itemList, foundOffer.getCostB());
			} catch (ImpossibleException oops) {
				TradePeripheral.debug(oops.getMessage());
				return MethodResult.of("impossible_copy_extraction");
			}

			if (!ItemUtil.canInsert(itemList, foundOffer.getResult()))
				return MethodResult.of("insufficient_space");

			try {
				ItemUtil.extract(handler, foundOffer.getCostA());
				ItemUtil.extract(handler, foundOffer.getCostB());
			} catch (ImpossibleException oops) {
				TradePeripheral.debug(oops.getMessage());
				return MethodResult.of("impossible_extraction");
			}

			try {
				ItemUtil.insert(handler, foundOffer.getResult());
			} catch (ImpossibleException oops) {
				TradePeripheral.debug(oops.getMessage());
				return MethodResult.of("impossible_insertion");
			}
		}

		return MethodResult.of(true);
	}
}
