package svenhjol.charm.feature.no_chat_unverified_message;

import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = Charm.MOD_ID,
    switchable = true,
    description = "Disables the 'Chat messages can't be verified' nag when the server does not enforce secure profile."
)
public class NoChatUnverifiedMessage extends CharmFeature {
}
