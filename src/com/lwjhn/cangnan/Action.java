package com.lwjhn.cangnan;

import com.alibaba.fastjson.JSONArray;
import com.lwjhn.domino.ArcBase;
import com.lwjhn.domino.BaseUtils;
import com.lwjhn.domino.DatabaseCollection;
import com.lwjhn.domino2sql.config.DefaultConfig;
import com.lwjhn.util.*;
import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.EmbeddedObject;
import org.apache.commons.io.FileUtils;

import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.BaseElement;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: lwjhn
 * @Date: 2020-11-27
 * @Description: com.lwjhn.cangnan
 * @Version: 1.0
 */
public class Action extends ArcBase {
    protected DatabaseCollection databaseCollection = null;
    protected OutputStream outputStream = null;
    protected ZipOutputStream zipOutputStream = null;
    protected CharSequence delimiter = ";";
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Action(DatabaseCollection databaseCollection) throws Exception {
        if ((this.databaseCollection = databaseCollection) == null)
            throw new Exception("parameter databaseCollection is null ! ");
    }

    public Action(DatabaseCollection databaseCollection, CharSequence delimiter, SimpleDateFormat dateFormat) throws Exception {
        if ((this.databaseCollection = databaseCollection) == null)
            throw new Exception("parameter databaseCollection is null ! ");
        this.delimiter = delimiter;
        this.dateFormat = dateFormat;
    }

    public Action createZipFile(File out, Charset charset) throws Exception {
        this.closeQuiet();
        outputStream = new FileOutputStream(out);
        zipOutputStream = new ZipOutputStream(outputStream, charset);
        return this;
    }

    public void evaluateDocument(org.dom4j.Document template, lotus.domino.Document doc) throws Exception {
        org.dom4j.Element root = template.getRootElement();
        java.util.Iterator childs = root.elementIterator();
        String name;
        org.dom4j.Attribute attribute;
        org.dom4j.Element node = null;
        doc.replaceItemValue("Server", doc.getParentDatabase().getServer());
        doc.replaceItemValue("DbName", doc.getParentDatabase().getFilePath());
        doc.replaceItemValue("UNID", doc.getUniversalID());
        while (childs.hasNext()) {
            node = (org.dom4j.Element) childs.next();
            if (!((attribute = node.attribute("name")) == null || "".equals(name = attribute.getValue().trim()))) {
                node.setText(getItemValue(doc, name));
            } else if (!((name = node.getText()) == null || "".equals(name.trim()))) {
                try {
                    node.setText(join(databaseCollection.getSession().evaluate(name, doc), delimiter, dateFormat));
                } catch (Exception err) {
                    throw new Exception("formual: " + name + System.lineSeparator() + getStackMsg(err));
                }
            }
        }
    }

    private String getItemValue(lotus.domino.Document doc, String name) {
        lotus.domino.Item item = null;
        try {
            if ((item = doc.getFirstItem(name)) == null) return "";
            switch (item.getType()) {
                case lotus.domino.Item.RICHTEXT:
                    return ((lotus.domino.RichTextItem) item).getUnformattedText();
                default:
                    return item.getText();
            }
        } catch (Exception e) {
            return "";
        } finally {
            BaseUtils.recycle(item);
        }
    }

    public void outputAttachmentByPDoc(org.dom4j.Document bill, Document srcdoc, ModuleType moduleType, String root) throws Exception {
        outputAttachmentByPDoc(bill, srcdoc, moduleType,
                new AttachmentWriter() {
                    public Element action(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception {
                        return outputAttachment(eo, ftype, root + "/" + filename);
                    }
                });
    }

    public void outputAttachmentByPDoc(org.dom4j.Document bill, Document srcdoc, ModuleType moduleType, String root, ZipOutputStream zipOutputStream) throws Exception {
        outputAttachmentByPDoc(bill, srcdoc, moduleType,
                new AttachmentWriter() {
                    public Element action(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception {
                        return outputAttachment(eo, ftype, root + "/" + filename, zipOutputStream);
                    }
                });
    }

    public void outputAttachmentByPDoc(org.dom4j.Document bill, Document srcdoc, ModuleType moduleType, AttachmentWriter writer) throws Exception {
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

            query = "(Form = \"Attachment\" | Form = \"MSS\") & DOCUNID = \"" + srcdoc.getUniversalID() + "\""; //!@Contains(Form;"DelForm")
            if (!((key = srcdoc.getItemValueString("UniAppUnid")) == null || "".equals(key)))
                query += " & DOCUNID = \"" + key + "\"";

            mssdb = databaseCollection.getDatabase(srv, dbpath);
            if (mssdb == null || !mssdb.isOpen())
                throw new Exception("can't open database ! " + srv + " !! " + dbpath);

            outputAttachment(bill.getRootElement(), mssdc = mssdb.search(query, null, 0), moduleType, writer);
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdc);
        }
    }

    private void outputAttachment(org.dom4j.Element billRoot, DocumentCollection mssdc, ModuleType moduleType, AttachmentWriter writer) throws Exception {
        Document mssdoc = null, msstdoc = null;
        try {
            mssdoc = mssdc.getFirstDocument();
            while (mssdoc != null) {
                outputAttachment(billRoot, mssdoc, moduleType, writer);
                mssdoc = mssdc.getNextDocument(msstdoc = mssdoc);
                recycle(msstdoc);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(mssdoc, msstdoc);
        }
    }

    public void outputAttachment(org.dom4j.Element billRoot, Document mssdoc, ModuleType moduleType, String root) throws Exception {
        outputAttachment(billRoot, mssdoc, moduleType,
                new AttachmentWriter() {
                    public Element action(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception {
                        return outputAttachment(eo, ftype, root + "/" + filename);
                    }
                });
    }

    public void outputAttachment(org.dom4j.Element billRoot, Document mssdoc, ModuleType moduleType, String root, ZipOutputStream zipOutputStream) throws Exception {
        outputAttachment(billRoot, mssdoc, moduleType,
                new AttachmentWriter() {
                    public Element action(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception {
                        return outputAttachment(eo, ftype, root + "/" + filename, zipOutputStream);
                    }
                });
    }

    public void outputAttachment(org.dom4j.Element billRoot, Document mssdoc, ModuleType moduleType, AttachmentWriter writer) throws Exception {
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

                billRoot.add(writer.action(eo, moduleType.getTypes()
                        .get(AttachForm.enumValueOfQuiet(mssdoc.getItemValueString("Form")))
                        .get(i < 0 ? AttachNormal.UNNORMAL : AttachNormal.NORMAL), filename));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            recycle(eo);
        }
    }

    public interface AttachmentWriter {
        public org.dom4j.Element action(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception;
    }

    public static final Pattern PATTERN_EXT = Pattern.compile("\\.([0-9a-z]+$)", Pattern.CASE_INSENSITIVE);

    public org.dom4j.Element outputAttachment(EmbeddedObject eo, AttachmentType ftype, String filename) throws Exception {
        OutputStream os = null;
        String filepath;
        try {
            new File(filepath = FileOperator.getAvailablePath(FileOperator.getFileFolderByRegex(filename), ftype.getFolder())).mkdirs();
            filename = FileOperator.getFileNameByRegex(filename);
            return outputAttachment(eo, os = new FileOutputStream(filepath + "/" + filename), ftype, filename);
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(os);
        }
    }

    public org.dom4j.Element outputAttachment(EmbeddedObject eo, AttachmentType ftype, String filename, ZipOutputStream zipOutputStream) throws Exception {
        try {
            zipOutputStream.flush();
            zipOutputStream.putNextEntry(new ZipEntry(
                    FileOperator.getAvailablePath(FileOperator.getFileFolderByRegex(filename), ftype.getFolder(), filename = FileOperator.getFileNameByRegex(filename))
                            .replaceAll("^[/\\\\]+", "")
            ));
            return outputAttachment(eo, zipOutputStream, ftype, filename);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public org.dom4j.Element outputAttachment(InputStream is, ZipOutputStream zipOutputStream, AttachmentType ftype, String filename, Date createTime, long size) throws Exception {
        try {
            zipOutputStream.flush();
            zipOutputStream.putNextEntry(new ZipEntry(
                    FileOperator.getAvailablePath(FileOperator.getFileFolderByRegex(filename), ftype.getFolder(), filename = FileOperator.getFileNameByRegex(filename))
                            .replaceAll("^[/\\\\]+", "")
            ));
            return outputAttachment(is, (OutputStream) zipOutputStream, ftype, filename, createTime, size);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public org.dom4j.Element outputAttachment(EmbeddedObject eo, OutputStream os, AttachmentType ftype, String filename) throws Exception {
        InputStream is = null;
        try {
            return outputAttachment(is = eo.getInputStream(), os, ftype, filename, eo.getFileCreated().toJavaDate(), eo.getFileSize());
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    public org.dom4j.Element outputAttachment(InputStream is, OutputStream os, AttachmentType ftype, String filename, Date createTime, long size) throws Exception {
        Matcher matcher;
        try {
            org.dom4j.Element root = new BaseElement("fileinfo").addAttribute("title", "材料信息");
            root.addElement("CLMC").addAttribute("title", "材料名称").setText(filename);
            root.addElement("CLLX").addAttribute("title", "材料类型").setText(ftype.getAlias());
            root.addElement("SQFS").addAttribute("title", "收取方式").setText("电子收取");
            root.addElement("WBSSM").addAttribute("title", "未收取说明");
            org.dom4j.Element node = root.addElement("detailinfo").addAttribute("title", "计算机文件详细信息");
            node.addElement("WJM").addAttribute("title", "计算机文件名").setText(filename);
            node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(createTime));
            node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(createTime));
            node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(size));
            matcher = PATTERN_EXT.matcher(filename);
            node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText(matcher.find(1) ? matcher.group(1) : "");
            node.addElement("WJSZZY").addAttribute("title", "文件数字摘要值").setText("MD5:" + DigestUtils.copyFileAndDigest(
                    is,
                    os
            ));
            return root;
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }

    public org.dom4j.Document createAttachmentBill(Charset charset) throws Exception {
        org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
        document.setXMLEncoding(charset.toString());
        org.dom4j.Element root = document.addElement("description")
                .addAttribute("title", "材料收取清单");
        return document;
    }

    public org.dom4j.Document createFlowDocument(Document srcdoc, Charset charset) throws Exception {
        String srv, dbpath, unid = srcdoc.getUniversalID();
        DocumentCollection mssdc = null;
        Document doc = null, tdoc = null;
        try {
            if ((srv = srcdoc.getItemValueString("MSSSERVER")) == null || "".equals(srv))
                srv = srcdoc.getParentDatabase().getServer();
            if (srv == null || (dbpath = srcdoc.getItemValueString("MssFlow")) == null)
                throw new Exception("can not find item of MssFlow from document . " + srcdoc.getUniversalID());

            mssdc = databaseCollection.getDatabase(srv, dbpath).search(
                    "Form=\"FlowForm\" & DOCUNID = \"" + unid + "\"",
                    null, 0
            );
            org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
            document.setXMLEncoding(charset.toString());
            org.dom4j.Element root = document.addElement("description")
                    .addAttribute("title", "流程信息元数据");

            doc = mssdc.getFirstDocument();
            while (doc != null) {
                createFlowElement(root, doc, srcdoc);
                doc = mssdc.getNextDocument(tdoc = doc);
                BaseUtils.recycle(tdoc);
                tdoc = null;
            }
            return document;
        } catch (Exception e) {
            throw e;
        } finally {

            BaseUtils.recycle(doc, tdoc, mssdc);
        }
    }

    private void createFlowElement(org.dom4j.Element rootFlow, Document mssdoc, Document srcdoc) throws Exception {
        String srv, dbpath, unid = srcdoc.getUniversalID();
        DocumentCollection mssdc = null;
        Document doc = null, tdoc = null;
        org.dom4j.Element node, root = new BaseElement("process").addAttribute("title", "流程信息");
        try {
            root.addElement("YWXW").addAttribute("title", "业务行为").setText(mssdoc.getItemValueString("C_UNITNAME"));
            root.addElement("CLRY").addAttribute("title", "处理人员").setText(mssdoc.getItemValueString("C_USERNAME"));
            root.addElement("CLBM").addAttribute("title", "处理部门").setText(mssdoc.getItemValueString("C_USERDEPTNAME"));

            if ((srv = srcdoc.getItemValueString("MSSSERVER")) == null || "".equals(srv))
                srv = srcdoc.getParentDatabase().getServer();
            if (srv == null || (dbpath = srcdoc.getItemValueString("MssOpinion")) == null || !mssdoc.hasItem("C_UNITINDEX"))
                throw new Exception("find item of MssOpinion or C_UNITINDEX is null . ");

            mssdc = databaseCollection.getDatabase(srv, dbpath).search(
                    "Form=\"Opinion\" & PARENTUNID = \"" + unid + "\" & OPINIONUSER=\"" + mssdoc.getItemValueString("C_USERID")
                            + "\" & (UNITINDEX=\"" + mssdoc.getItemValueString("C_UNITINDEX") + "\" | !@IsAvailable(UNITINDEX))",
                    null, 0
            );
            if (mssdc.getCount() < 1)
                throw new Exception("can not find opinion. ");
            doc = mssdc.getFirstDocument();
            while (doc != null) {
                node = (org.dom4j.Element) root.clone();
                node.addElement("CLSJ").addAttribute("title", "处理时间").setText(
                        DefaultConfig.DateFormat.format(doc.getCreated().toJavaDate())
                );  //OPINIONTIME
                node.addElement("CLYJ").addAttribute("title", "处理意见").setText(doc.hasItem("OPINIONBODY") ? doc.getItemValueString("OPINIONBODY") : "");
                rootFlow.add(node);
                doc = mssdc.getNextDocument(tdoc = doc);
                BaseUtils.recycle(tdoc);
                tdoc = null;
            }
            root = null;
        } catch (Exception e) {
            if (root != null) {
                root.addElement("CLSJ").addAttribute("title", "处理时间");
                root.addElement("CLYJ").addAttribute("title", "处理意见");
                rootFlow.add(root);
            }
        } finally {
            BaseUtils.recycle(doc, tdoc, mssdc);
        }
    }

    private String join(Iterable elements, CharSequence delimiter, SimpleDateFormat dateFormat) throws Exception {
        if (elements == null) return "";
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

    private void closeQuiet() {
        AutoCloseableBase.close(zipOutputStream, outputStream);
    }

    @Override
    public void recycle() {
        close();
    }
}
