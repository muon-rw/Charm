package svenhjol.charm_core.enums;

import javax.annotation.Nullable;

public enum ListType {
    TAG(10);

    private final int num;

    ListType(int num) {
        this.num = num;
    }

    @Nullable
    public ListType byNum(int num) {
        for (ListType type : ListType.values()) {
            if (type.getNum() == num) {
                return type;
            }
        }

        return null;
    }

    public int getNum() {
        return num;
    }
}
