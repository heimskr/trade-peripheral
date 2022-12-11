package gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral;

import com.lothrazar.villagertools.ModRegistry;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.addons.computercraft.operations.SphereOperationContext;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.BlockEntityPeripheralOwner;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.IPeripheralOwner;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.PocketPeripheralOwner;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.TurtlePeripheralOwner;
import gay.heimskr.tradeperipheral.common.blocks.base.PeripheralBlockEntity;
import gay.heimskr.tradeperipheral.common.blocks.blockentities.TraderEntity;
import gay.heimskr.tradeperipheral.common.configuration.APConfig;
import gay.heimskr.tradeperipheral.common.util.LuaConverter;
import gay.heimskr.tradeperipheral.lib.peripherals.BasePeripheral;
import gay.heimskr.tradeperipheral.lib.peripherals.IPeripheralPlugin;
import gay.heimskr.tradeperipheral.common.addons.computercraft.operations.SphereOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
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

		public List<Object> toLua() {
			List<Object> costs = Arrays.asList(Arrays.asList(costA.getDescriptionId(), costA.getCount()));
			if (!(costB.getItem() instanceof AirItem))
				costs.add(Arrays.asList(costA.getDescriptionId(), costA.getCount()));
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

				for (var offer: villager.getOffers()) {
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
}
