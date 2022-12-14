package gay.heimskr.tradeperipheral.common.fluid;

import cofh.lib.fluid.FluidCoFH;
import cofh.thermal.lib.common.ThermalItemGroups;
import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidAttributes;


public class SoulSauceFluid extends FluidCoFH {
    public static SoulSauceFluid create() {
        return new SoulSauceFluid("soul_sauce", "tradeperipheral:block/fluids/soul_sauce_still", "tradeperipheral:block/fluids/soul_sauce_flow");
    }

    protected SoulSauceFluid(String key, String stillTexture, String flowTexture) {
        super(Registration.FLUIDS, key, FluidAttributes.builder(new ResourceLocation(stillTexture), new ResourceLocation(flowTexture)).density(1100).viscosity(3000).sound(SoundEvents.BOTTLE_FILL, SoundEvents.BOTTLE_EMPTY));
        this.bucket = Registration.ITEMS.register(bucket(key), () -> new BucketItem(stillFluid, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(TradePeripheral.TAB)));
        properties.bucket(this.bucket);
    }
}
