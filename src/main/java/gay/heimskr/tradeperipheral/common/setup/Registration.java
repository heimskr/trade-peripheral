package gay.heimskr.tradeperipheral.common.setup;

import cofh.lib.util.DeferredRegisterCoFH;
import dan200.computercraft.api.pocket.PocketUpgradeSerialiser;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TradePeripheral.MOD_ID);
    public static final DeferredRegisterCoFH<Item> ITEMS = DeferredRegisterCoFH.create(ForgeRegistries.ITEMS, TradePeripheral.MOD_ID);
    public static final DeferredRegisterCoFH<Fluid> FLUIDS = DeferredRegisterCoFH.create(ForgeRegistries.FLUIDS, TradePeripheral.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TradePeripheral.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, TradePeripheral.MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, TradePeripheral.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, TradePeripheral.MOD_ID);
    public static final DeferredRegister<TurtleUpgradeSerialiser<?>> TURTLE_SERIALIZER = DeferredRegister.create(TurtleUpgradeSerialiser.REGISTRY_ID, TradePeripheral.MOD_ID);
    public static final DeferredRegister<PocketUpgradeSerialiser<?>> POCKET_SERIALIZER = DeferredRegister.create(PocketUpgradeSerialiser.REGISTRY_ID, TradePeripheral.MOD_ID);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        FLUIDS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        CONTAINER_TYPES.register(modEventBus);
        POI_TYPES.register(modEventBus);
        VILLAGER_PROFESSIONS.register(modEventBus);
        TURTLE_SERIALIZER.register(modEventBus);
        POCKET_SERIALIZER.register(modEventBus);

        Blocks.register();
        BlockEntityTypes.register();
        Items.register();
        ContainerTypes.register();
        CCRegistration.register();
        FluidTypes.register();
    }
}
