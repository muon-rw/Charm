package svenhjol.charm.module.azalea_wood;

import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.module.extra_boats.CharmBoatEntity;

public class AzaleaItems {
    public static class AzaleaBoatItem extends CharmBoatItem {
        public AzaleaBoatItem(CharmCommonModule module) {
            super(module, "azalea_boat", CharmBoatEntity.BoatType.AZALEA);
        }
    }

    public static class AzaleaSignItem extends CharmSignItem {
        public AzaleaSignItem(CharmCommonModule module) {
            super(module, "azalea_sign", AzaleaWood.SIGN_BLOCK, AzaleaWood.WALL_SIGN_BLOCK);
        }
    }
}
