package gay.heimskr.tradeperipheral.common.items;

import gay.heimskr.tradeperipheral.common.items.base.BaseItem;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TPItem extends BaseItem {

    @Nullable
    private final ResourceLocation turtleID;
    @Nullable
    private final ResourceLocation pocketID;
    private final Supplier<Boolean> enabledSup;

    public TPItem(Properties properties, @Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID, Supplier<Boolean> enabledSup) {
        super(properties);
        this.turtleID = turtleID;
        this.pocketID = pocketID;
        this.enabledSup = enabledSup;
    }

    public TPItem(@Nullable ResourceLocation turtleID, @Nullable ResourceLocation pocketID, Supplier<Boolean> enabledSup) {
        super();
        this.turtleID = turtleID;
        this.pocketID = pocketID;
        this.enabledSup = enabledSup;
    }

    @Override
    public boolean isEnabled() {
        return enabledSup.get();
    }
}
