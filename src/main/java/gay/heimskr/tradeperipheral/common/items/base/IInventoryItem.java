package gay.heimskr.tradeperipheral.common.items.base;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IInventoryItem {

    MenuProvider createContainer(Player playerEntity, ItemStack itemStack);

}
