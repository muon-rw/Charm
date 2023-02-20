package svenhjol.charm_core.base;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.helper.TextHelper;

import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public abstract class CharmFeature {
    private String modId = "";
    private String description = "";
    private int priority = 0;
    private boolean enabled = true;
    private boolean enabledInConfig = true;
    private boolean enabledByDefault = true;
    private boolean switchable = true;

    /**
     * Set up pre-registration stuff.
     * Typically used for enums.
     */
    public void preRegister() {
        // no op
    }

    /**
     * Set up any registrations that need access to all the other loaded features.
     * This method is always executed even if the feature is disabled.
     */
    public void register() {
        // no op
    }

    /**
     * Set up anything post registration.
     * This method is always executed even if the feature is disabled.
     */
    public void runAlways() {
        // no op
    }

    /**
     * Set up features and resources that should only be present if the feature is enabled.
     */
    public void runWhenEnabled() {
        // no op
    }

    /**
     * Remove features and resources when the feature is disabled.
     */
    public void runWhenDisabled() {
        // no op
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public ResourceLocation getFeatureId() {
        return new ResourceLocation(getModId(),
            TextHelper.upperCamelToSnake(getName()).toLowerCase(Locale.ROOT));
    }

    public boolean isEnabled() {
        return enabled && enabledInConfig;
    }

    public boolean isSwitchable() {
        return switchable;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    public String getModId() {
        return modId;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public List<BooleanSupplier> checks() {
        return List.of();
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSwitchable(boolean switchable) {
        this.switchable = switchable;
    }

    public void setEnabledByDefault(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }

    public void setEnabledInConfig(boolean enabledInConfig) {
        this.enabledInConfig = enabledInConfig;
    }
}
