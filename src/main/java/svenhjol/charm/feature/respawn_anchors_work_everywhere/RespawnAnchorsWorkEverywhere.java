package svenhjol.charm.feature.respawn_anchors_work_everywhere;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(enabledByDefault = false, description = """
    The repsawn anchor can be used in any dimension.
    This feature changes core gameplay so is disabled by default.""")
public final class RespawnAnchorsWorkEverywhere extends CommonFeature {
    public RespawnAnchorsWorkEverywhere(CommonLoader loader) {
        super(loader);
    }
}
