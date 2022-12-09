package gay.heimskr.tradeperipheral.common.items.base;

import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.util.EnumColor;
import gay.heimskr.tradeperipheral.common.util.TranslationUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseBlockItem extends BlockItem {
//    private Component description;

    public BaseBlockItem(Block blockIn, Properties properties) {
        super(blockIn, properties.tab(TradePeripheral.TAB));
    }

    public BaseBlockItem(Block blockIn) {
        super(blockIn, new Properties().tab(TradePeripheral.TAB));
    }

//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable Level levelIn, List<Component> tooltip, TooltipFlag flagIn) {
//        super.appendHoverText(stack, levelIn, tooltip, flagIn);
//        if (!KeyBindings.DESCRIPTION_KEYBINDING.isDown()) {
//            tooltip.add(EnumColor.buildTextComponent(new TranslatableComponent("item.tradeperipheral.tooltip.show_desc", KeyBindings.DESCRIPTION_KEYBINDING.getTranslatedKeyMessage())));
//        } else {
//            tooltip.add(EnumColor.buildTextComponent(getDescription()));
//        }
//        if (!isEnabled())
//            tooltip.add(EnumColor.buildTextComponent(new TranslatableComponent("item.tradeperipheral.tooltip.disabled")));
//    }

//    public @NotNull Component getDescription() {
//        if (description == null) description = TranslationUtil.itemTooltip(getDescriptionId());
//        return description;
//    }

//    public abstract boolean isEnabled();
}
