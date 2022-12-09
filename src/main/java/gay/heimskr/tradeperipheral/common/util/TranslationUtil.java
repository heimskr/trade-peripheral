package gay.heimskr.tradeperipheral.common.util;

import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraft.network.chat.TranslatableComponent;

public class TranslationUtil {

    public static TranslatableComponent itemTooltip(String descriptionId) {
        int lastIndex = descriptionId.lastIndexOf('.');
        return new TranslatableComponent(String.format(
                "%s.tooltip.%s",
                descriptionId.substring(0, lastIndex).replaceFirst("^block", "item"),
                descriptionId.substring(lastIndex + 1)
        ));
    }

    public static String turtle(String name) {
        return String.format("turtle.%s.%s", TradePeripheral.MOD_ID, name);
    }

    public static String pocket(String name) {
        return String.format("pocket.%s.%s", TradePeripheral.MOD_ID, name);
    }
}
