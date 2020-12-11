package com.lwjhn.cangnan;

import com.lwjhn.domino.DatabaseCollection;
import com.lwjhn.util.FileOperator;
import com.lwjhn.util.XmlBase;
import com.rjsoft.archive.RJUitilDSXml;
import lotus.domino.Document;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: lwjhn
 * @Date: 2020-12-10
 * @Description: com.lwjhn.util
 * @Version: 1.0
 */
public class Archive {
    private Action action = null;
    protected OutputStream outputStream = null;
    protected ZipOutputStream zipOutputStream = null;
    ByteArrayOutputStream swapStream = null;

    private static final String rjdsFileName = "办理单.html";

    public Archive(DatabaseCollection databaseCollection) throws Exception {
        action = new Action(databaseCollection);
        swapStream = new ByteArrayOutputStream();
    }

    public void archive(Document srcdoc, org.dom4j.Document template, File out, Charset charset, ModuleType moduleType) throws Exception {
        org.dom4j.Document document = null;
        String root = "";
        try {
            root = FileOperator.getFileAliasByRegex(out.getName());
            createZipFile(out, charset);
            action.evaluateDocument(document = (org.dom4j.Document) template.clone(), srcdoc);
            zipOutputStream.putNextEntry(new ZipEntry(root + "/" + "基本信息.xml"));
            XmlBase.write(document, zipOutputStream, charset);
            zipOutputStream.flush();
            action.outputAttachmentByPDoc(document = action.createAttachmentBill(charset), srcdoc, moduleType, root, zipOutputStream);

            swapStream.reset();
            RJUitilDSXml.parseDSHtml(srcdoc, action.databaseCollection.getSession(), swapStream, charset);
            document.getRootElement().add(action.outputAttachment(
                    new ByteArrayInputStream(swapStream.toByteArray()),
                    zipOutputStream,
                    moduleType == ModuleType.DISPATCH ? AttachmentType.D_YB : AttachmentType.R_YB,
                    root + "/" + rjdsFileName,
                    new Date(),
                    swapStream.size()
            ));

            zipOutputStream.putNextEntry(new ZipEntry(root + "/" + "材料收取清单.xml"));
            XmlBase.write(document, zipOutputStream, charset);
            zipOutputStream.flush();

            if ((document = action.createFlowDocument(srcdoc, charset)) == null) return;
            zipOutputStream.putNextEntry(new ZipEntry(root + "/" + "流程信息.xml"));
            XmlBase.write(document, zipOutputStream, charset);
            zipOutputStream.flush();

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public void close() {
        if (zipOutputStream != null) try {
            zipOutputStream.close();
        } catch (Exception e) {
        }
        if (outputStream != null) try {
            outputStream.close();
        } catch (Exception e) {
        }
    }

    public void createZipFile(File out, Charset charset) throws Exception {
        this.close();
        outputStream = new FileOutputStream(out);
        zipOutputStream = new ZipOutputStream(outputStream);
    }
}
