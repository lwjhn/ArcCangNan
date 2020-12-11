package com.lwjhn.domino2mht;

import com.lwjhn.mht.DocMht;
import com.lwjhn.util.Replicator;

/**
 * @Author: lwjhn
 * @Date: 2020-12-4
 * @Description: com.lwjhn.domino2mht
 * @Version: 1.0
 */
public class OutputDocument extends DocMht {
    Replicator replicator = null;
    private OutputDocument() throws Exception {
        super(new Replicator() {
            @Override
            public String replace(String key) {
                return null;
            }
        });
    }
}
