package gay.heimskr.tradeperipheral.common.util;

import gay.heimskr.tradeperipheral.TradePeripheral;
//import gay.heimskr.tradeperipheral.common.configuration.TPConfig;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class Platform {

    public static Optional<Object> maybeLoadIntegration(final String modid, final String path) {
        if (!ModList.get().isLoaded(modid)) {
            TradePeripheral.LOGGER.info(String.format("%s not loaded, skip integration loading", modid));
            return Optional.empty();
        }
        return maybeLoadIntegration(path);
    }

    public static Optional<Object> maybeLoadIntegration(final String path) {
        try {
            Class<?> clazz = Class.forName(TradePeripheral.class.getPackage().getName() + ".common.addons." + path);
            return Optional.of(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException loadException) {
//            if (TPConfig.GENERAL_CONFIG.enableDebugMode.get())
                loadException.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
