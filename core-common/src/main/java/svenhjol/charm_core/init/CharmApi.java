package svenhjol.charm_core.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm_core.CharmCore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue", "unchecked"})
public class CharmApi {
    private static final Logger LOGGER = LogManager.getLogger("CharmApi");

    private static final List<Object> PROVIDERS = new ArrayList<>();

    public static void registerProvider(Object provider) {
        CharmCore.LOG.debug(CharmApi.class, "Registering class " + provider.getClass().getSimpleName() + " as a data provider.");
        PROVIDERS.add(provider);
    }

    public static <T> List<T> getProviders(Class<T> type) {
        var providers = PROVIDERS.stream()
            .filter(o -> type.isAssignableFrom(o.getClass()))
            .map(o -> (T)o).toList();

//        CharmCore.LOG.debug(ApiHandler.class, providers.size() + " unique data provider(s) for API " + type.getSimpleName());
        return providers;
    }

    /**
     * Flatten all provider data into a single list.
     * @param type API type interface
     * @param flatten Method to flatten/combine streams.
     * @return All provider data.
     */
    public static <T, S> List<S> getProviderData(Class<T> type, Function<? super T, ? extends Stream<S>> flatten) {
        var data = getProviders(type).stream().flatMap(flatten).toList();

//        CharmCore.LOG.debug(ApiHandler.class, data.size() + " provided data item(s) for API " + type.getSimpleName());
        return data;
    }
}
