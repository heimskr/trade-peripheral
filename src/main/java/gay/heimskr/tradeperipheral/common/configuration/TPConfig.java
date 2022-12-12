//package gay.heimskr.tradeperipheral.common.configuration;
//
//import com.electronwill.nightconfig.core.file.CommentedFileConfig;
//import net.minecraftforge.fml.ModContainer;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.config.ConfigFileTypeHandler;
//import net.minecraftforge.fml.config.ModConfig;
//import net.minecraftforge.fml.loading.FMLPaths;
//
//import java.nio.file.Path;
//import java.util.function.Function;
//
//public class TPConfig extends ModConfig {
//
//    public static final ConfigFileHandler CONFIG_FILE_HANDLER = new ConfigFileHandler();
//
//    public static final GeneralConfig GENERAL_CONFIG = new GeneralConfig();
//    public static final PeripheralsConfig PERIPHERALS_CONFIG = new PeripheralsConfig();
//    public static final WorldConfig WORLD_CONFIG = new WorldConfig();
//
//    public TPConfig(IAPConfig config, ModContainer container) {
//        super(config.getType(), config.getConfigSpec(), container, "tradeperipheral/" + config.getFileName() + ".toml");
//    }
//
//    public static void register(ModLoadingContext context) {
//        //Creates the config folder
//        FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve("tradeperipheral"), "tradeperipheral");
//
//        ModContainer modContainer = context.getActiveContainer();
//        modContainer.addConfig(new TPConfig(GENERAL_CONFIG, modContainer));
//        modContainer.addConfig(new TPConfig(PERIPHERALS_CONFIG, modContainer));
//        modContainer.addConfig(new TPConfig(WORLD_CONFIG, modContainer));
//    }
//
//    @Override
//    public ConfigFileTypeHandler getHandler() {
//        return CONFIG_FILE_HANDLER;
//    }
//
//    public static class ConfigFileHandler extends ConfigFileTypeHandler {
//
//        public static Path getPath(Path path) {
//            if (path.endsWith("serverconfig")) return FMLPaths.CONFIGDIR.get();
//
//            return path;
//        }
//
//        @Override
//        public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
//            return super.reader(getPath(configBasePath));
//        }
//
//        @Override
//        public void unload(Path configBasePath, ModConfig config) {
//            super.unload(getPath(configBasePath), config);
//        }
//    }
//}
