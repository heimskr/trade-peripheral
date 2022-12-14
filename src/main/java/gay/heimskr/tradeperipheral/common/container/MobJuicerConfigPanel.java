package gay.heimskr.tradeperipheral.common.container;

import cofh.core.client.gui.IGuiAccess;
import cofh.core.client.gui.element.panel.ConfigPanel;
import cofh.lib.api.control.IReconfigurable;
import cofh.lib.api.control.ITransferControllable;
import net.minecraft.core.Direction;

import java.util.function.Supplier;

public class MobJuicerConfigPanel extends ConfigPanel {
	public MobJuicerConfigPanel(IGuiAccess gui, IReconfigurable reconfig, Supplier<Direction> facingSup) {
		super(gui, reconfig, facingSup);
	}

	public MobJuicerConfigPanel(IGuiAccess gui, ITransferControllable transfer, IReconfigurable reconfig, Supplier<Direction> facingSup) {
		super(gui, transfer, reconfig, facingSup);
	}

	protected MobJuicerConfigPanel(IGuiAccess gui, int sideIn, ITransferControllable transfer, IReconfigurable reconfig, Supplier<Direction> facingSup) {
		super(gui, sideIn, transfer, reconfig, facingSup);
	}

	@Override
	protected void handleSideChange(Direction side, int mouseButton) {
		if (side != Direction.UP)
			super.handleSideChange(side, mouseButton);
	}
}
