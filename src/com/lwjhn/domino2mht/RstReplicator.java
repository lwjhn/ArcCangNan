package com.lwjhn.domino2mht;

import lotus.domino.Document;

/**
 * @Author: lwjhn
 * @Date: 2020-12-4
 * @Description: com.lwjhn.domino2mht
 * @Version: 1.0
 */
public class RstReplicator extends LotusReplicator {
    public RstReplicator(Document document) throws Exception {
        super(document);
    }

    public String replace(String key) throws Exception {
        return super.replace(key).replaceAll("(?i)<table\\s", "<table width=3D100% ");
    }
}
