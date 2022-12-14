package gay.heimskr.tradeperipheral.common.container;

import cofh.core.client.gui.ContainerScreenCoFH;
import cofh.core.client.gui.element.panel.PanelBase;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.client.gui.ThermalGuiHelper;
import cofh.thermal.lib.client.gui.MachineScreenReconfigurable;
import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static cofh.core.util.helpers.GuiHelper.*;

public class MobJuicerScreen extends MachineScreenReconfigurable<MobJuicerContainer> {

	public static final String TEX_PATH = TradePeripheral.MOD_ID + ":textures/gui/mob_juicer.png";
	public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

	public MobJuicerScreen(MobJuicerContainer container, Inventory inv, Component titleIn) {
		super(container, inv, container.blockEntity, StringHelper.getTextComponent("block.tradeperipheral.mob_juicer"));
		texture = TEXTURE;
		info = generatePanelInfo("info.tradeperipheral.mob_juicer");
		name = "mob_juicer";
	}

	@Override
	public void init() {
		super.init();

		// Why is this necessary.
		try {
			Class cls = ContainerScreenCoFH.class;
			Field panels_field = cls.getDeclaredField("panels");
			panels_field.setAccessible(true);
			var private_panels = (ArrayList<PanelBase>) panels_field.get(this);
			var last = private_panels.get(private_panels.size() - 1);
			private_panels.remove(private_panels.size() - 1);
			private_panels.remove(private_panels.size() - 1);
	        this.addPanel((new MobJuicerConfigPanel(this, this.tile, this.tile, () -> this.tile.getFacing())).addConditionals(ThermalGuiHelper.createDefaultMachineConfigs(this, this.name, this.tile)));
			private_panels.add(last);
		} catch (Exception err) {
			TradePeripheral.LOGGER.error("Error hacking panels: " + err.toString());
			err.printStackTrace();
		}

		addElement(setClearable(createMediumOutputFluidStorage(this, 151, 22, tile.getTank(0), tile), tile, 0));
	}

}
