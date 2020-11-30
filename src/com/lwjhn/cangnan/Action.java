package com.lwjhn.cangnan;

import com.alibaba.fastjson.JSONObject;
import com.lwjhn.domino.ArcBase;
import com.lwjhn.domino.BaseUtils;
import com.lwjhn.domino.DatabaseCollection;
import com.lwjhn.domino2sql.config.DefaultConfig;
import com.lwjhn.util.AutoCloseableBase;
import com.lwjhn.util.DigestUtils;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import org.apache.commons.io.FileUtils;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultElement;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;

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

    public void outputAttachment(org.dom4j.Document bill, lotus.domino.Document srcdoc, String filePath) throws Exception {
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

            outputAttachment(bill.getRootElement(), mssdc = mssdb.search(query, null, 0), filePath);
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdc);
        }
    }

    private void outputAttachment(org.dom4j.Element billRoot, DocumentCollection mssdc, String filePath) throws Exception {
        Document mssdoc = null, msstdoc = null;
        try {
            mssdoc = mssdc.getFirstDocument();
            while (mssdoc != null) {
                outputAttachment(billRoot, mssdoc, filePath);
                mssdoc = mssdc.getNextDocument(msstdoc = mssdoc);
                recycle(msstdoc);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdoc, msstdoc);
        }
    }

    private void outputAttachment(org.dom4j.Element billRoot, Document mssdoc, String filePath) throws Exception {
        try {
/*
<fileinfo title="材料信息">
    <CLMC title="材料名称">高兴夫关于《关于防控新型冠状病毒肺炎疫情应急物资费用结算工作进展情况的 再次通报》的批示.pdf</CLMC>
    <CLLX title="材料类型">承办单</CLLX>
    <SQFS title="收取方式">电子收取</SQFS>
    <WBSSM title="未收取说明"></WBSSM>
    <detailinfo title="计算机文件详细信息">
      <WJM title="计算机文件名">高兴夫关于《关于防控新型冠状病毒肺炎疫情应急物资费用结算工作进展情况的 再次通报》的批示.pdf</WJM>
      <CJSJ title="计算机文件创建时间">2020-07-24 09:23:30</CJSJ>
      <XGSJ title="计算机文件修改时间">2020-07-24 09:23:30</XGSJ>
      <WJDX title="计算机文件大小">780.34KB</WJDX>
      <GSXX title="计算机文件格式信息">PDF</GSXX>
      <WJSZZY title="文件数字摘要值">MD5:9574874b55a35486fa67284f0e014cd4</WJSZZY>
    </detailinfo>
  </fileinfo>
 */

        } catch (Exception e) {
            throw e;
        } finally {
            //recycle(mssdoc);
        }
    }

    public org.dom4j.Element outputAttachment(EmbeddedObject eo, File output) throws Exception {
        Matcher matcher = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            org.dom4j.Element node = new BaseElement("detailinfo").addAttribute("title", "计算机文件详细信息");
            node.addElement("WJM").addAttribute("title", "计算机文件名").setText(output.getName());
            node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(eo.getFileCreated().toJavaDate()));
            node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(eo.getFileModified().toJavaDate()));
            node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(eo.getFileSize()));
            matcher = DefaultConfig.PATTERN_EXT.matcher(eo.getName());
            node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText((matcher.find() ? matcher.group() : ""));
            node.addElement("WJSZZY").addAttribute("title", "文件数字摘要值").setText("MD5:" + DigestUtils.copyFileAndDigest(
                    is = eo.getInputStream(),
                    os = new FileOutputStream(output)
            ));
            return node;
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
