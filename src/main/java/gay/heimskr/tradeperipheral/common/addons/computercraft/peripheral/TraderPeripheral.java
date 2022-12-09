package gay.heimskr.tradeperipheral.common.addons.computercraft.peripheral;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TraderPeripheral extends BasePeripheral<IPeripheralOwner> {

    public static final String TYPE = "environmentDetector";
    private static final List<Function<IPeripheralOwner, IPeripheralPlugin>> PLUGINS = new LinkedList<>();

    protected TraderPeripheral(IPeripheralOwner owner) {
        super(TYPE, owner);
        owner.attachOperation(SphereOperation.SCAN_ENTITIES);
        for (Function<IPeripheralOwner, IPeripheralPlugin> plugin : PLUGINS)
            addPlugin(plugin.apply(owner));
    }

    public TraderPeripheral(PeripheralBlockEntity<?> tileEntity) {
        this(new BlockEntityPeripheralOwner<>(tileEntity).attachFuel());
    }

    public TraderPeripheral(ITurtleAccess turtle, TurtleSide side) {
        this(new TurtlePeripheralOwner(turtle, side).attachFuel(1));
    }

    public TraderPeripheral(IPocketAccess pocket) {
        this(new PocketPeripheralOwner(pocket));
    }

    private static int estimateCost(int radius) {
        if (radius <= SphereOperation.SCAN_ENTITIES.getMaxFreeRadius()) {
            return 0;
        }
        if (radius > SphereOperation.SCAN_ENTITIES.getMaxCostRadius()) {
            return -1;
        }
        return SphereOperation.SCAN_ENTITIES.getCost(SphereOperationContext.of(radius));
    }

    public static void addIntegrationPlugin(Function<IPeripheralOwner, IPeripheralPlugin> plugin) {
        PLUGINS.add(plugin);
    }

    @Override
    public boolean isEnabled() {
        return APConfig.PERIPHERALS_CONFIG.enableEnergyDetector.get();
    }

    @LuaFunction(mainThread = true)
    public final String getBiome() {
        String biomeName = getLevel().getBiome(getPos()).value().getRegistryName().toString();
        String[] biome = biomeName.split(":");
        return biome[1];
    }

    @LuaFunction(mainThread = true)
    public final int getSkyLightLevel() {
        return getLevel().getBrightness(LightLayer.SKY, getPos().offset(0, 1, 0));
    }

    @LuaFunction(mainThread = true)
    public final int getBlockLightLevel() {
        return getLevel().getBrightness(LightLayer.BLOCK, getPos().offset(0, 1, 0));
    }

    @LuaFunction(mainThread = true)
    public final int getDayLightLevel() {
        Level level = getLevel();
        int i = level.getBrightness(LightLayer.SKY, getPos().offset(0, 1, 0)) - level.getSkyDarken();
        float f = level.getSunAngle(1.0F);
        if (i > 0) {
            float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
            f = f + (f1 - f) * 0.2F;
            i = Math.round((float) i * Mth.cos(f));
        }
        i = Mth.clamp(i, 0, 15);
        return i;
    }

    @LuaFunction(mainThread = true)
    public final long getTime() {
        return getLevel().getDayTime();
    }

    @LuaFunction(mainThread = true)
    public final boolean isSlimeChunk() {
        ChunkPos chunkPos = new ChunkPos(getPos());
        return WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, ((WorldGenLevel) getLevel()).getSeed(), 987234911L).nextInt(10) == 0;
    }

    @LuaFunction(mainThread = true)
    public final String getDimensionProvider() {
        return getLevel().dimension().getRegistryName().getNamespace();
    }

    @LuaFunction(mainThread = true)
    public final String getDimensionName() {
        return getLevel().dimension().getRegistryName().getPath();
    }

    @LuaFunction(mainThread = true)
    public final String getDimensionPaN() {
        return getLevel().dimension().getRegistryName().toString();
    }

    @LuaFunction(mainThread = true)
    public final boolean isDimension(String dimension) {
        return getLevel().dimension().getRegistryName().getPath().equals(dimension);
    }

    @LuaFunction(mainThread = true)
    public final Set<String> listDimensions() {
        Set<String> dimensions = new HashSet<>();
        ServerLifecycleHooks.getCurrentServer().getAllLevels().forEach(serverWorld -> dimensions.add(serverWorld.dimension().getRegistryName().getPath()));
        return dimensions;
    }

    @LuaFunction(mainThread = true)
    public final MethodResult scanEntities(@Nonnull IComputerAccess access, @Nonnull IArguments arguments) throws LuaException {
        int radius = arguments.getInt(0);
        return withOperation(SphereOperation.SCAN_ENTITIES, new SphereOperationContext(radius), context -> {
            if (radius > SphereOperation.SCAN_ENTITIES.getMaxCostRadius()) {
                return MethodResult.of(null, "Radius is exceed max value");
            }
            return null;
        }, context -> {
            BlockPos pos = owner.getPos();
            AABB box = new AABB(pos);
            List<Map<String, Object>> entities = new ArrayList<>();
            getLevel().getEntities((Entity) null, box.inflate(radius), LivingEntity.class::isInstance).forEach(entity -> entities.add(LuaConverter.completeEntityWithPositionToLua(entity, ItemStack.EMPTY, pos)));
            return MethodResult.of(entities);
        }, null);
    }

    @LuaFunction
    public final MethodResult scanCost(int radius) {
        int estimatedCost = estimateCost(radius);
        if (estimatedCost < 0) {
            return MethodResult.of(null, "Radius is exceed max value");
        }
        return MethodResult.of(estimatedCost);
    }
}
