package com.lwjhn.cangnan;

/**
 * @Author: lwjhn
 * @Date: 2020-11-30
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public enum AttachForm {
    MSS("正文"), ATTACHMENT("附件"), PROCESSING("阅办单");

    private String alias;

    public String getAlias() {
        return alias;
    }

    AttachForm(String name) {
        this.alias = name;
    }

    public static AttachForm enumValueOfQuiet(String name) {
        try {
            return name == null ? AttachForm.ATTACHMENT : AttachForm.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return AttachForm.ATTACHMENT;
        }
    }
}
