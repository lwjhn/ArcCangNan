package com.lwjhn.cangnan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lwjhn.domino.ArcBase;
import com.lwjhn.domino.BaseUtils;
import com.lwjhn.domino.DatabaseCollection;
import com.lwjhn.domino2sql.config.DefaultConfig;
import com.lwjhn.util.AutoCloseableBase;
import com.lwjhn.util.DigestUtils;
import com.lwjhn.util.FileOperator;
import com.sun.xml.internal.ws.api.message.Attachment;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultElement;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: lwjhn
 * @Date: 2020-11-27
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public class Action extends ArcBase {
    private DatabaseCollection databaseCollection = null;

    public Action(DatabaseCollection databaseCollection) throws Exception {
        if ((this.databaseCollection = databaseCollection) == null)
            throw new Exception("parameter databaseCollection is null ! ");
    }

    public void output(org.dom4j.Document document, File output) throws Exception {
        output(document, output, Charset.forName("UTF-8"));
    }

    public void output(org.dom4j.Document document, File output, Charset charset) throws Exception {
        FileOutputStream fo = null;
        Writer writer = null;
        org.dom4j.io.XMLWriter xmlwriter = null;
        try {
            org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
            format.setEncoding(charset.toString());
            fo = new FileOutputStream(output);
            writer = new OutputStreamWriter(fo, charset);
            xmlwriter = new org.dom4j.io.XMLWriter(writer, format);
            xmlwriter.write(document);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (xmlwriter != null) xmlwriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (writer != null) writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fo != null) fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void evaluateDocument(org.dom4j.Document template, lotus.domino.Document doc) throws Exception {
        org.dom4j.Element root = template.getRootElement();
        java.util.Iterator childs = root.elementIterator();
        org.dom4j.Element node = null;
        String formula = null;
        doc.replaceItemValue("Server", doc.getParentDatabase().getServer());
        doc.replaceItemValue("DbName", doc.getParentDatabase().getFilePath());
        doc.replaceItemValue("UNID", doc.getUniversalID());
        while (childs.hasNext()) {
            node = (org.dom4j.Element) childs.next();
            node.setText(join(databaseCollection.getSession().evaluate(node.getText(), doc), null, null));
        }
    }

    public void outputAttachment(org.dom4j.Document bill, lotus.domino.Document srcdoc, String filePath, ModuleType moduleType) throws Exception {
        String srv = null, dbpath = null, key = null, query = "";
        Database mssdb = null;
        DocumentCollection mssdc = null;
        try {
            if ((srv = srcdoc.getItemValueString("MSSSERVER")) == null || "".equals(srv))
                srv = srcdoc.getParentDatabase().getServer();
            if (srv == null || (dbpath = srcdoc.getItemValueString("MSSDATABASE")) == null)
                throw new Exception("can not find item of mssdatabase from document . " + srcdoc.getUniversalID());
            mssdb = databaseCollection.getDatabase(srv, dbpath);
            if (mssdb == null || !mssdb.isOpen())
                throw new Exception("can't open attachment database ! " + srv + " !! " + dbpath);

            query = "(Form = \"Attachment\" | Form = \"MSS\") &  & DOCUNID = \"" + srcdoc.getUniversalID() + "\""; //!@Contains(Form;"DelForm")
            if (!((key = srcdoc.getItemValueString("UniAppUnid")) == null || "".equals(key)))
                query += " & DOCUNID = \"" + key + "\"";

            mssdb = databaseCollection.getDatabase(srv, dbpath);
            if (mssdb == null || !mssdb.isOpen())
                throw new Exception("can't open database ! " + srv + " !! " + dbpath);

            outputAttachment(bill.getRootElement(), mssdc = mssdb.search(query, null, 0), filePath, moduleType);
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdc);
        }
    }

    private void outputAttachment(org.dom4j.Element billRoot, DocumentCollection mssdc, String filePath, ModuleType moduleType) throws Exception {
        Document mssdoc = null, msstdoc = null;
        try {
            mssdoc = mssdc.getFirstDocument();
            while (mssdoc != null) {
                outputAttachment(billRoot, mssdoc, filePath, moduleType);
                mssdoc = mssdc.getNextDocument(msstdoc = mssdoc);
                recycle(msstdoc);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdoc, msstdoc);
        }
    }

    public void outputAttachment(org.dom4j.Element billRoot, Document mssdoc, String filepath, ModuleType moduleType) throws Exception {
        EmbeddedObject eo = null;
        String filename;
        Matcher matcher;
        AttachmentType ftype = null;
        int i;
        try {
            Vector<String> all = databaseCollection.getSession().evaluate("@AttachmentNames", mssdoc);
            if (all == null || all.size() < 1) return;
            Vector<String> files = mssdoc.hasItem("AttachFile") ? mssdoc.getItemValue("AttachFile") : new Vector(),
                    vAlias = mssdoc.hasItem("AttachTitle") ? mssdoc.getItemValue("AttachTitle") : new Vector();

            for (String file : all) {
                if ((i = files.indexOf(file)) < 0 || vAlias.size() < i || "".equals(filename = String.valueOf(vAlias.get(i)))) {
                    filename = file;
                } else {
                    matcher = DefaultConfig.PATTERN_EXT.matcher(file);
                    filename += (matcher.find() ? matcher.group() : "");
                }
                recycle(eo);
                if ((eo = mssdoc.getAttachment(file)) == null) continue;

                billRoot.add(outputAttachment(eo, filepath, moduleType.getTypes()
                        .get(AttachForm.enumValueOfQuiet(mssdoc.getItemValueString("Form")))
                        .get(i < 0 ? AttachNormal.UNNORMAL : AttachNormal.NORMAL), filename));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(eo);
        }
    }

    public static final Pattern PATTERN_EXT = Pattern.compile("\\.([0-9a-z]+$)", Pattern.CASE_INSENSITIVE);

    public org.dom4j.Element outputAttachment(EmbeddedObject eo, String filepath, AttachmentType ftype, String filename) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        Matcher matcher = null;
        try {
            new File(filepath = FileOperator.getAvailablePath(filepath, ftype.getFolder())).mkdirs();

            org.dom4j.Element root = new BaseElement("fileinfo").addAttribute("title", "材料信息");
            root.addElement("CLMC").addAttribute("title", "材料名称").setText(filename);
            root.addElement("CLLX").addAttribute("title", "材料类型").setText(ftype.getAlias());
            root.addElement("SQFS").addAttribute("title", "收取方式").setText("电子收取");
            root.addElement("WBSSM").addAttribute("title", "未收取说明");
            org.dom4j.Element node = root.addElement("detailinfo").addAttribute("title", "计算机文件详细信息");
            node.addElement("WJM").addAttribute("title", "计算机文件名").setText(filename);
            node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(eo.getFileCreated().toJavaDate()));
            node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(eo.getFileModified().toJavaDate()));
            node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(eo.getFileSize()));
            matcher = PATTERN_EXT.matcher(eo.getName());
            node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText(matcher.find(1) ? matcher.group(1) : "");
            node.addElement("WJSZZY").addAttribute("title", "文件数字摘要值").setText("MD5:" + DigestUtils.copyFileAndDigest(
                    is = eo.getInputStream(),
                    os = new FileOutputStream(filepath + "/" + filename)
            ));
            return root;
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(is, os);
        }
    }

    public org.dom4j.Document createAttachmentBill(Charset charset) throws Exception {
        org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
        document.setXMLEncoding(charset.toString());
        org.dom4j.Element root = document.addElement("description");
        root.addAttribute("title", "材料收取清单");
        return document;
    }

    private String join(Iterable elements, CharSequence delimiter, SimpleDateFormat dateFormat) throws Exception {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter == null ? DefaultConfig.String_Join_Delimiter : delimiter);
        for (Object cs : elements) {
            if (cs == null) {
                joiner.add("");
            } else if (cs instanceof lotus.domino.DateTime) {
                joiner.add((dateFormat == null ? DefaultConfig.DateFormat : dateFormat).format(((lotus.domino.DateTime) cs).toJavaDate()));
            } else {
                joiner.add(cs.toString());
            }
        }
        return joiner.toString();
    }

    @Override
    public void recycle() {

    }
}
