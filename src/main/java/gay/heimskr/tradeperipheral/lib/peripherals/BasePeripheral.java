package gay.heimskr.tradeperipheral.lib.peripherals;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.IPeripheralOwner;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.OperationAbility;
import gay.heimskr.tradeperipheral.common.addons.computercraft.owner.PeripheralOwnerAbility;
import gay.heimskr.tradeperipheral.common.util.LuaConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BasePeripheral<O extends IPeripheralOwner> implements IBasePeripheral<O>, IDynamicPeripheral {

    protected final List<IComputerAccess> connectedComputers = new ArrayList<>();
    protected final String type;
    protected final O owner;
    protected final List<BoundMethod> pluggedMethods = new ArrayList<>();
    protected boolean initialized = false;
    protected List<IPeripheralPlugin> plugins = null;
    protected String[] methodNames = new String[0];

    public BasePeripheral(String type, O owner) {
        this.type = type;
        this.owner = owner;
    }

    protected void buildPlugins() {
        if (!initialized) {
            initialized = true;
            this.pluggedMethods.clear();
            if (plugins != null) plugins.forEach(plugin -> {
                if (plugin.isSuitable(this)) pluggedMethods.addAll(plugin.getMethods());
            });
            owner.getAbilities().forEach(ability -> {
                if (ability instanceof IPeripheralPlugin peripheralPlugin)
                    pluggedMethods.addAll(peripheralPlugin.getMethods());
            });
            this.methodNames = pluggedMethods.stream().map(BoundMethod::getName).toArray(String[]::new);
        }
    }

    protected void addPlugin(@NotNull IPeripheralPlugin plugin) {
        if (plugins == null) plugins = new LinkedList<>();
        plugins.add(plugin);
        IPeripheralOperation<?>[] operations = plugin.getOperations();
        if (operations != null) {
            OperationAbility operationAbility = owner.getAbility(PeripheralOwnerAbility.OPERATION);
            if (operationAbility == null)
                throw new IllegalArgumentException("This is not possible to attach plugin with operations to not operationable owner");
            for (IPeripheralOperation<?> operation : operations)
                operationAbility.registerOperation(operation);
        }
    }

    @Override
    public List<IComputerAccess> getConnectedComputers() {
        return connectedComputers;
    }

    @Nullable
    @Override
    public Object getTarget() {
        return owner;
    }

    @NotNull
    @Override
    public String getType() {
        return type;
    }

    @Override
    public void attach(@NotNull IComputerAccess computer) {
        connectedComputers.add(computer);
    }

    @Override
    public void detach(@NotNull IComputerAccess computer) {
        connectedComputers.remove(computer);
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return iPeripheral == this;
    }

    @Override
    public O getPeripheralOwner() {
        return owner;
    }

    @LuaFunction
    public final String getName() {
        return owner.getCustomName();
    }

    public Map<String, Object> getPeripheralConfiguration() {
        Map<String, Object> data = new HashMap<>();
        owner.getAbilities().forEach(ability -> ability.collectConfiguration(data));
        return data;
    }

    @LuaFunction
    public final Map<String, Object> getConfiguration() {
        return getPeripheralConfiguration();
    }

    protected BlockPos getPos() {
        return owner.getPos();
    }

    protected Level getLevel() {
        return owner.getLevel();
    }

    protected Direction validateSide(String direction) throws LuaException {
        String dir = direction.toUpperCase(Locale.ROOT);

        return LuaConverter.getDirection(owner.getFacing(), dir);
    }

    @Override
    @NotNull
    public String @NotNull [] getMethodNames() {
        if (!initialized) buildPlugins();
        return methodNames;
    }

    @Override
    @NotNull
    public MethodResult callMethod(@NotNull IComputerAccess access, @NotNull ILuaContext context, int index, @NotNull IArguments arguments) throws LuaException {
        if (!initialized) buildPlugins();
        return pluggedMethods.get(index).apply(access, context, arguments);
    }

    protected <T> MethodResult withOperation(IPeripheralOperation<T> operation, T context, @Nullable IPeripheralCheck<T> check, IPeripheralFunction<T, MethodResult> method, @Nullable Consumer<T> successCallback) throws LuaException {
        return withOperation(operation, context, check, method, successCallback, null);
    }

    protected <T> MethodResult withOperation(IPeripheralOperation<T> operation, T context, @Nullable IPeripheralCheck<T> check, IPeripheralFunction<T, MethodResult> method, @Nullable Consumer<T> successCallback, @Nullable BiConsumer<MethodResult, OperationAbility.FailReason> failCallback) throws LuaException {
        OperationAbility operationAbility = owner.getAbility(PeripheralOwnerAbility.OPERATION);
        if (operationAbility == null) throw new IllegalArgumentException("This shouldn't happen at all");
        return operationAbility.performOperation(operation, context, check, method, successCallback, failCallback);
    }
}
