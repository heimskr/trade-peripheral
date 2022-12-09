package gay.heimskr.tradeperipheral.common.items.base;

import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.util.EnumColor;
import gay.heimskr.tradeperipheral.common.util.TranslationUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseItem extends Item {
    private Component description;

    public BaseItem(Properties properties) {
        super(properties.tab(TradePeripheral.TAB));
    }

    public BaseItem() {
        super(new Properties().tab(TradePeripheral.TAB));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (worldIn.isClientSide)
            return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
        if (this instanceof IInventoryItem inventoryItem) {
            ServerPlayer serverPlayerEntity = (ServerPlayer) playerIn;
            ItemStack stack = playerIn.getItemInHand(handIn);
            NetworkHooks.openGui(serverPlayerEntity, inventoryItem.createContainer(playerIn, stack), buf -> buf.writeItem(stack));
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
//        if (!KeyBindings.DESCRIPTION_KEYBINDING.isDown()) {
//            tooltip.add(EnumColor.buildTextComponent(new TranslatableComponent("item.advancedperipherals.tooltip.show_desc", KeyBindings.DESCRIPTION_KEYBINDING.getTranslatedKeyMessage())));
//        } else {
//            tooltip.add(EnumColor.buildTextComponent(getDescription()));
//        }
//        if (!isEnabled())
//            tooltip.add(EnumColor.buildTextComponent(new TranslatableComponent("item.advancedperipherals.tooltip.disabled")));
    }


    public @NotNull Component getDescription() {
        if (description == null) description = TranslationUtil.itemTooltip(getDescriptionId());
        return description;
    }

    public abstract boolean isEnabled();

}
