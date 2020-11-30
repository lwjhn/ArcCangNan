package com.lwjhn.cangnan;

/**
 * @Author: lwjhn
 * @Date: 2020-11-30
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public enum AttachmentType {
    D_YG("签发稿"),
    D_ZW("版式文件"),
    R_ZW("电子收文件"),
    D_YB("拟办单"),
    R_YB("承办单"),
    C_FJ("附件","附件材料")
    ;

    private String alias;
    private String folder;

    public String getAlias() {
        return alias;
    }

    public String getFolder() {
        return folder;
    }

    private AttachmentType(String name) {
        this.folder = this.alias = name;
    }
    private AttachmentType(String name, String folder) {
        this.alias = name;
        this.folder = folder;
    }
}
