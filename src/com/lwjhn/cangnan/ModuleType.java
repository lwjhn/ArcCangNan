package com.lwjhn.cangnan;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: lwjhn
 * @Date: 2020-11-30
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public enum ModuleType {
    DISPATCH("发文", new HashMap<AttachForm, Map<AttachNormal, AttachmentType>>() {{
        put(AttachForm.MSS, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.D_ZW);
            put(AttachNormal.UNNORMAL, AttachmentType.D_YG);
        }});
        put(AttachForm.ATTACHMENT, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.C_FJ);
            put(AttachNormal.UNNORMAL, AttachmentType.C_FJ);
        }});
        put(AttachForm.PROCESSING, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.D_YB);
            put(AttachNormal.UNNORMAL, AttachmentType.D_YB);
        }});
    }}),
    RECEIVAL("收文", new HashMap<AttachForm, Map<AttachNormal, AttachmentType>>() {{
        put(AttachForm.MSS, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.R_ZW);
            put(AttachNormal.UNNORMAL, AttachmentType.R_ZW);
        }});
        put(AttachForm.ATTACHMENT, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.C_FJ);
            put(AttachNormal.UNNORMAL, AttachmentType.C_FJ);
        }});
        put(AttachForm.PROCESSING, new HashMap() {{
            put(AttachNormal.NORMAL, AttachmentType.R_YB);
            put(AttachNormal.UNNORMAL, AttachmentType.R_YB);
        }});
    }});

    private String name;
    private Map<AttachForm, Map<AttachNormal, AttachmentType>> types;

    public String getName() {
        return name;
    }

    public Map<AttachForm, Map<AttachNormal, AttachmentType>> getTypes() {
        return types;
    }

    ModuleType(String name, Map<AttachForm, Map<AttachNormal, AttachmentType>> types) {
        this.name = name;
        this.types = types;
    }

    public static ModuleType enumValueOfQuiet(String name) {
        try {
            return name == null ? ModuleType.DISPATCH : ModuleType.valueOf(name.toUpperCase());
        } catch (Exception e) {
            return ModuleType.DISPATCH;
        }
    }
}
