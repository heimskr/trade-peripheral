package gay.heimskr.tradeperipheral.common.data;

import gay.heimskr.tradeperipheral.TradePeripheral;
import gay.heimskr.tradeperipheral.common.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = TradePeripheral.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    private DataGenerators() {
    }

    @SubscribeEvent
    public static void genData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new BlockTagsProvider(generator, existingFileHelper, Registration.BLOCKS));
        generator.addProvider(new RecipesProvider(generator));
        generator.addProvider(new BlockLootTablesProvider(generator));
        generator.addProvider(new ModelProvider(generator, existingFileHelper));
    }
}
