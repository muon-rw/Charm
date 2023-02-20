package svenhjol.charm_core.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charm_core.helper.TextHelper;
import svenhjol.charm_core.proxy.ConfigHelperProxy;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public abstract class BaseMixinConfigPlugin implements IMixinConfigPlugin {
    protected String mixinPackage;
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");
    protected static final Map<String, Boolean> BLACKLISTED = new HashMap<>();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;

        // Prepare mixin blacklist
        var blacklist = ConfigHelperProxy.getConfigDir() + "/" + getModId() + "-mixin-blacklist.txt";
        if (new File(blacklist).exists()) {
            try {
                var stream = new FileInputStream(blacklist);
                var scanner = new Scanner(stream);
                while (scanner.hasNextLine()) {
                    BLACKLISTED.put(scanner.nextLine(), true);
                }
                scanner.close();
                stream.close();
            } catch(Exception e){
                LOGGER.warn("IO error when handling mixin blacklist: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Do the logic checking in onLoad. Fetching annotations here breaks everything.
        var simpleName = mixinClassName.substring(mixinPackage.length() + 1);
        var featureName = TextHelper.snakeToUpperCamel(simpleName.substring(0, simpleName.indexOf(".")));

        LOGGER.info("Processing mixin " + mixinClassName);

        // Always deny blacklisted entries.
        if (BLACKLISTED.containsKey(simpleName) && BLACKLISTED.get(simpleName)) {
            return false;
        }

        // Always allow accessors and events.
        if (targetClassName.contains("accessor") || targetClassName.contains("event")) {
            return true;
        }

        // Check if feature is disabled in config.
        if (!ConfigHelperProxy.isFeatureEnabled(getModId(), featureName)) {
            return false;
        }

        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // no op
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    protected abstract String getModId();
}