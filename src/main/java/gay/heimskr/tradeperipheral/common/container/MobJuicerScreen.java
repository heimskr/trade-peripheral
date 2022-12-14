package gay.heimskr.tradeperipheral.common.container;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.client.gui.ThermalGuiHelper;
import cofh.thermal.expansion.inventory.container.machine.MachinePyrolyzerContainer;
import cofh.thermal.lib.client.gui.MachineScreenReconfigurable;
import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

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

        addElement(createInputSlot(this, 44, 26, tile));

//        addElement(createOutputSlot(this, 107, 26, tile));
//        addElement(createOutputSlot(this, 125, 26, tile));
//        addElement(createOutputSlot(this, 107, 44, tile));
//        addElement(createOutputSlot(this, 125, 44, tile));

        addElement(setClearable(createMediumOutputFluidStorage(this, 151, 22, tile.getTank(0), tile), tile, 0));

        addElement(ThermalGuiHelper.createDefaultFluidProgress(this, 72, 35, PROG_ARROW_FLUID_RIGHT, tile));
        addElement(ThermalGuiHelper.createDefaultProgress(this, 72, 35, PROG_ARROW_RIGHT, tile));
        addElement(ThermalGuiHelper.createDefaultSpeed(this, 44, 44, SCALE_FLAME, tile));
    }

}
