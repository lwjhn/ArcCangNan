package com.lwjhn.domino2mht;

import com.lwjhn.domino.BaseUtils;
import com.lwjhn.util.Replicator;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.RichTextItem;

/**
 * @Author: lwjhn
 * @Date: 2020-12-4
 * @Description: com.lwjhn.domino2mht
 * @Version: 1.0
 */
public class LotusReplicator implements Replicator {
    public static String UnicodePrefix = "&#x";
    public static String UnicodeSuffix = ";";
    private Document document = null;
    private Item item = null;

    public LotusReplicator(Document document) throws Exception {
        if (document == null) throw new Exception("document is null .");
        this.document = document;
    }

    public static String escapeUnicode(String input) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < input.length(); i++) {
            int chr1 = (char) input.charAt(i);
            if (chr1 > 0x007F) {
                result.append(UnicodePrefix).append(Integer.toHexString(chr1)).append(UnicodeSuffix);
            } else {
                result.append(input.charAt(i));
            }
        }
        return result.toString();
    }

    @Override
    public String replace(String key) throws Exception {
        try {
            BaseUtils.recycle(item);
            if ((item = document.getFirstItem(key)) == null) return "";
            return escapeUnicode(
                    item.getType() == Item.RICHTEXT ?
                            ((RichTextItem) item).getUnformattedText() :
                            item.getText()
            );
        } catch (Exception e) {
            throw e;
        } finally {
            BaseUtils.recycle(item);
            item = null;
        }
    }
}
