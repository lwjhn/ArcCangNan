package com.lwjhn.cangnan;

/**
 * @Author: lwjhn
 * @Date: 2020-11-30
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public enum AttachNormal {
    NORMAL("normal"),
    UNNORMAL("unnormal");

    private String alias;

    public String getAlias() {
        return alias;
    }

    AttachNormal(String name) {
        this.alias = name;
    }

    public static AttachNormal enumValueOfQuiet(String name) {
        try {
            return name == null ? AttachNormal.UNNORMAL : AttachNormal.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return AttachNormal.UNNORMAL;
        }
    }
}
