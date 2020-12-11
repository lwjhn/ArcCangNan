package com.lwjhn.cangnan;

import com.lwjhn.domino.DatabaseCollection;
import com.lwjhn.util.FileOperator;
import com.lwjhn.util.XmlBase;
import lotus.domino.Document;

import java.io.File;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;

/**
 * @Author: lwjhn
 * @Date: 2020-12-10
 * @Description: com.lwjhn.util
 * @Version: 1.0
 */
public class Archive extends Action {
    public Archive(DatabaseCollection databaseCollection) throws Exception {
        super(databaseCollection);
    }

    public Action archive(Document srcdoc, org.dom4j.Document template, File out, Charset charset, ModuleType moduleType) throws Exception {
        org.dom4j.Document document = null;
        String root = "";
        try {
            root = FileOperator.getFileAliasByRegex(out.getName());
            this.createZipFile(out, charset);
            evaluateDocument(document = (org.dom4j.Document) template.clone(), srcdoc);
            this.zipOutputStream.putNextEntry(new ZipEntry(root + "/" + "基本信息.xml"));
            XmlBase.write(document, this.zipOutputStream, charset);
            this.zipOutputStream.flush();
            outputAttachmentByPDoc(document = this.createAttachmentBill(charset), srcdoc, moduleType, root);
        } catch (Exception e) {
            throw e;
        } finally {
            this.recycle();
        }
        return this;
    }
}
