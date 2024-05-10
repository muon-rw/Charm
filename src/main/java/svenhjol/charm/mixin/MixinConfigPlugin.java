package svenhjol.charm.mixin;

import svenhjol.charm.Charm;
import svenhjol.charmony.helper.ConfigHelper;

import java.util.List;
import java.util.function.Predicate;

import com.google.common.base.CaseFormat;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charmony.helper.ConfigHelper.Side;

public class MixinConfigPlugin implements IMixinConfigPlugin {
    protected String mixinPackage;
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");
    protected static final Map<String, Boolean> BLACKLISTED = new HashMap();
    protected static final List<String> ALL_MIXINS = new ArrayList();

    public MixinConfigPlugin() {
    }

    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        String blacklist = this.getBlacklistPath();
        File blacklistFile = new File(blacklist);
        if (blacklistFile.exists()) {
            try {
                FileInputStream stream = new FileInputStream(blacklist);
                Scanner scanner = new Scanner(stream);

                while(scanner.hasNextLine()) {
                    BLACKLISTED.put(scanner.nextLine(), true);
                }

                scanner.close();
                stream.close();
            } catch (Exception var6) {
                LOGGER.warn("IO error when handling mixin blacklist: " + var6.getMessage());
            }
        }

    }

    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String simpleName = mixinClassName.substring(this.mixinPackage.length() + 1);
        String featureName = snakeToUpperCamel(simpleName.substring(0, simpleName.indexOf(".")));
        boolean debug = ConfigHelper.isDebugEnabled();
        Iterator var6 = this.runtimeBlacklist().iterator();


        while(var6.hasNext()) {
            Predicate<String> predicate = (Predicate)var6.next();
            boolean isBlacklisted = predicate.test(featureName);
            // Only update if not already blacklisted or if the current predicate marks it as blacklisted
            BLACKLISTED.put(simpleName, BLACKLISTED.getOrDefault(simpleName, false) || isBlacklisted);
        }

        if (!ALL_MIXINS.contains(simpleName)) {
            ALL_MIXINS.add(simpleName);
        }

        if (ConfigHelper.isCompatEnabled() && !simpleName.startsWith("accessor")) {
            LOGGER.warn("Compat mode skipping mixin " + mixinClassName);
            return false;
        } else if (BLACKLISTED.containsKey(simpleName) && (Boolean)BLACKLISTED.get(simpleName)) {
            LOGGER.warn("Blacklisted mixin " + mixinClassName);
            return false;
        } else {
            String common = ConfigHelper.getFilename(this.modId(), Side.COMMON);
            String client = ConfigHelper.getFilename(this.modId(), Side.CLIENT);
            boolean enabledInCommon = ConfigHelper.isFeatureEnabled(common, featureName);
            boolean enabledInClient = ConfigHelper.isFeatureEnabled(client, featureName);
            boolean valid = enabledInCommon && enabledInClient;
            if (debug) {
                if (valid) {
                    LOGGER.info("Enabled mixin " + mixinClassName);
                } else {
                    LOGGER.info("Disabled mixin " + mixinClassName);
                }
            }

            return valid;
        }
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    protected String modId() {
        return Charm.ID;
    }

    protected String getBlacklistPath() {
        Path var10000 = ConfigHelper.getConfigDir();
        return "" + var10000 + "/" + this.modId() + "-mixin-blacklist.txt";
    }


    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of(
                feature -> feature.equals("ColoredGlintSmithingTemplates") && ConfigHelper.isModLoaded("optifabric"),
                feature -> feature.equals("NoChatUnverifiedMessage") && ConfigHelper.isModLoaded("chatsigninghider"),
                feature -> feature.equals("ExtraStackables") && ConfigHelper.isModLoaded("allstackable"),
                feature -> (feature.equals("UnlimitedRepairCost")
                        || feature.equals("ShowRepairCost")
                        || feature.equals("StrongerAnvils"))
                        && ConfigHelper.isModLoaded("zenith")
        );
    }

    public static String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }
}
