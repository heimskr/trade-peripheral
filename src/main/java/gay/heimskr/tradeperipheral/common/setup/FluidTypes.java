package gay.heimskr.tradeperipheral.common.setup;

import cofh.thermal.core.fluid.*;
import gay.heimskr.tradeperipheral.common.fluid.SoulSauceFluid;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Supplier;

import static cofh.core.util.helpers.FluidHelper.BOTTLE_DRAIN_MAP;
import static cofh.core.util.helpers.FluidHelper.BOTTLE_FILL_MAP;
import static cofh.lib.util.Constants.BOTTLE_VOLUME;
import static cofh.thermal.core.ThermalCore.ITEMS;

public class FluidTypes {
    private FluidTypes() {}

    public static void register() {
        SOUL_SAUCE_FLUID = SoulSauceFluid.create().still();
    }

    public static void setup() {
    }

    public static Supplier<ForgeFlowingFluid> SOUL_SAUCE_FLUID;
}
