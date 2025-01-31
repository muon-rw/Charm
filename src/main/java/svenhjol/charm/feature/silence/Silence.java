package svenhjol.charm.feature.silence;

import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.helper.ConfigHelper;

/**
 * Silence must have publicly accessible static variables for mixins to function properly.
 */
@Feature(enabledByDefault = false, description = """
    Disables some nag messages and telemetry.
    Some data removal may be considered controversial so this feature is disabled by default.""")
public final class Silence extends CommonFeature {
    public Silence(CommonLoader loader) {
        super(loader);
    }

    @Configurable(name = "Disable chat message verification dialog", description = """
        If true, disables the 'Chat messages can't be verified' dialog message when the server does not enforce secure profile.
        This only applies if you set 'enforce-secure-profile' to true in server.properties""")
    private static boolean disableChatMessageVerification = true;

    @Configurable(name = "Disable experimental screen dialog", description = """
        If true, disables the 'Experimental' warning dialog that appears when opening a world with experimental features enabled.""")
    private static boolean disableExperimental = true;

    @Configurable(name = "Disable telemetry", description = """
        If true, prevents the client telemetry manager from ever sending any messages back to the mothership.
        Telemetry includes your game session, game version, operating system and launcher.""")
    private static boolean disableTelemetry = true;

    @Configurable(name = "Downgrade data fixer registered error", description = """
        If true, downgrades the 'No data fixer registered' error to an info message.
        This isn't really an error for modded entities.""")
    private static boolean downgradeDataFixerError = true;

    @Configurable(name = "Disable development mode connections", description = """
        If true, disables realms and other API connections when running in the development environment.
        Setting this to true doesn't do anything if you are playing in a launcher.""")
    private static boolean disableDevEnvironmentConnections = true;

    public static boolean disableChatMessageVerification() {
        return disableChatMessageVerification;
    }

    public static boolean disableExperimental() {
        return disableExperimental;
    }

    public static boolean disableTelemetry() {
        return disableTelemetry;
    }

    public static boolean downgradeDataFixerError() {
        return downgradeDataFixerError;
    }

    public static boolean disableDevEnvironmentConnections() {
        return disableDevEnvironmentConnections
            && ConfigHelper.isDevEnvironment();
    }
}
