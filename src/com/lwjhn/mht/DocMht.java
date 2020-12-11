package com.lwjhn.mht;

import com.lwjhn.util.AutoCloseableBase;
import com.lwjhn.util.Replicator;
import com.lwjhn.util.StringTemplate;
import com.lwjhn.util.StringTemplateIO;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: lwjhn
 * @Date: 2020-12-3
 * @Description: com.lwjhn.mht
 * @Version: 1.0
 */
public class DocMht extends StringTemplateIO {
    private static String _not_line_regex = "\\=$";
    private static Pattern pattern_not_line = Pattern.compile(_not_line_regex);

    public DocMht(Replicator replicator) throws Exception {
        super(replicator);
    }

    public DocMht transform(BufferedReader input, BufferedWriter output) throws Exception {
        StringBuffer _tbuff = null;
        String line;
        do {
            if (_tbuff == null) _tbuff = new StringBuffer();
            if ((line = input.readLine()) != null) {
                if (pattern_not_line.matcher(line).find()) {
                    _tbuff.append(line.replaceAll(_not_line_regex, ""));
                    continue;
                } else {
                    _tbuff.append(line).append("\n");
                }
            }
            if (_tbuff.length() < 1) continue;
            output.write(StringTemplate.process(_tbuff, replicator).toString());
            output.flush();
            _tbuff = null;
        } while (line != null);
        return this;
    }
}
