package gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral;

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
import gay.heimskr.tradeperipheral.common.configuration.APConfig;
import gay.heimskr.tradeperipheral.common.util.LuaConverter;
import gay.heimskr.tradeperipheral.lib.peripherals.BasePeripheral;
import gay.heimskr.tradeperipheral.lib.peripherals.IPeripheralPlugin;
import gay.heimskr.tradeperipheral.common.addons.computercraft.operations.SphereOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
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
	private static final List<Function<IPeripheralOwner, IPeripheralPlugin>> PLUGINS = new LinkedList<>();
	private static final Logger LOGGER = TradePeripheral.LOGGER;

	protected TraderPeripheral(IPeripheralOwner owner) {
		super(TYPE, owner);
		owner.attachOperation(SphereOperation.SCAN_ENTITIES);
		for (Function<IPeripheralOwner, IPeripheralPlugin> plugin : PLUGINS)
			addPlugin(plugin.apply(owner));
	}

	public TraderPeripheral(PeripheralBlockEntity<?> tileEntity) {
		this(new BlockEntityPeripheralOwner<>(tileEntity).attachFuel());
	}

	@Override
	public boolean isEnabled() {
		return APConfig.PERIPHERALS_CONFIG.enableEnergyDetector.get();
	}

//    @LuaFunction(mainThread = true)
//    public final MethodResult scanEntities(@Nonnull IComputerAccess access, @Nonnull IArguments arguments) throws LuaException {
//        int radius = arguments.getInt(0);
//        return withOperation(SphereOperation.SCAN_ENTITIES, new SphereOperationContext(radius), context -> {
//            if (radius > SphereOperation.SCAN_ENTITIES.getMaxCostRadius()) {
//                return MethodResult.of(null, "Radius is exceed max value");
//            }
//            return null;
//        }, context -> {
//            BlockPos pos = owner.getPos();
//            AABB box = new AABB(pos);
//            List<Map<String, Object>> entities = new ArrayList<>();
//            getLevel().getEntities((Entity) null, box.inflate(radius), LivingEntity.class::isInstance).forEach(entity -> entities.add(LuaConverter.completeEntityWithPositionToLua(entity, ItemStack.EMPTY, pos)));
//            return MethodResult.of(entities);
//        }, null);
//    }

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
			return Arrays.asList(
				Arrays.asList(costA.getDescriptionId(), costA.getCount()),
				Arrays.asList(costB.getDescriptionId(), costB.getCount()),
				uses,
				max
			);
		}
	}

	@LuaFunction(mainThread = true)
	public final MethodResult listTrades() {
		var villagers = new ArrayList<Villager>();

		getLevel().getEntities((Entity) null, new AABB(owner.getPos()).inflate(0., 2., 0.), Villager.class::isInstance).forEach(entity -> {
			if (entity instanceof Villager) {
				villagers.add((Villager) entity);
			}
		});

		if (villagers.size() == 0)
			return MethodResult.of("no_villagers");

		if (villagers.size() == 1) {
			List<Object> out = new ArrayList<>();
			villagers.get(0).getOffers().forEach(offer -> {
				out.add(new Trade(offer).toLua());
			});
			return MethodResult.of(out);
		}

		return MethodResult.of("multiple_villagers");
	}
}
