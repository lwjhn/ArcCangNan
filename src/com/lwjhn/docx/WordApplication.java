package com.lwjhn.docx;

import com.lwjhn.util.AutoCloseableBase;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @Author: lwjhn
 * @Date: 2020-12-2
 * @Description: com.lwjhn.utils
 * @Version: 1.0
 */
public class WordApplication implements AutoCloseable {
    private ZipFile docxFile = null;
    private ZipEntry documentXML = null;
    private InputStream documentXMLIS = null;
    private SAXReader saxReader = new SAXReader();
    private Document document = null;
    private OutputFormat format = OutputFormat.createPrettyPrint();
    private static String DOC_FILE_PATH = "word/document.xml";
    private static final int BUFFER_SIZE = 2 * 1024;

    private Map<String, String> declareNamespaces = null;

    public WordApplication() {

    }

    public WordApplication(File word) throws Exception {
        this.loadDocument(word);
    }

    public void loadDocument(File word) throws Exception {
        if (!(word.exists() && word.isFile())) throw new Exception("word file is not existence , or not file .");
        if (!word.canRead()) throw new Exception("word file can not read .");

        this.close();
        docxFile = new ZipFile(word);
        documentXML = docxFile.getEntry(DOC_FILE_PATH);
        document = saxReader.read(documentXMLIS = docxFile.getInputStream(documentXML));

        declareNamespaces = new HashMap<>();
        List<Namespace> dns = document.getRootElement().declaredNamespaces();
        for (Namespace ns : dns)
            declareNamespaces.put(ns.getPrefix(), ns.getURI());
    }

    public void getNodeInnerText(Element element, StringBuilder response) {
        response.append(element.getText());
        Iterator childs = element.elementIterator();
        org.dom4j.Element el = null;
        while (childs.hasNext()) {
            el = (org.dom4j.Element) childs.next();
            getNodeInnerText(el, response);
        }
    }

    public String getBookMarkValue(String name) throws Exception {
        Element last, first = (Element) document.selectSingleNode("//w:bookmarkStart[@w:name='" + name + "']");
        if (first == null) return null;
        Element parent = first.getParent();
        int index = parent.indexOf(first);
        String id = first.attribute("id").getValue();
        StringBuilder res = new StringBuilder();
        for (int i = index + 1; i < parent.nodeCount(); i++) {
            last = (Element) parent.node(i);
            if (last.matches("//w:bookmarkEnd[@w:id=\"" + id + "\"]"))
                break;
            getNodeInnerText(last, res);
        }
        return res.toString();
    }

    public WordApplication setBookMark(String name, String content) throws Exception {
        Element last, first = (Element) document.selectSingleNode("//w:bookmarkStart[@w:name='" + name + "']");
        if (first == null) throw new Exception("can not find the book mark of " + name);
        Element parent = first.getParent();
        int index = parent.indexOf(first);
        String id = first.attribute("id").getValue();
        for (int i = index + 1; i < parent.nodeCount(); i++) {
            last = (Element) parent.node(i);
            if (last.matches("//w:bookmarkEnd[@w:id='" + id + "']"))
                break;
            parent.remove(last);
        }
        parent.content().add(index + 1, createWrt(content, first.getNamespace()));
        return this;
    }

    private void copyZipEntry(InputStream in, ZipOutputStream docxOutFile, ZipEntry zipEntry) throws Exception {
        try {
            docxOutFile.putNextEntry(zipEntry);
            int len;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((len = in.read(buf)) != -1) {
                docxOutFile.write(buf, 0, len);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (docxOutFile != null) docxOutFile.closeEntry();
            } catch (Exception e) {
            }
            AutoCloseableBase.close(in);
        }
    }

    private void copyZipEntry(byte[] in, ZipOutputStream docxOutFile, ZipEntry zipEntry) throws Exception {
        try {
            docxOutFile.putNextEntry(zipEntry);
            docxOutFile.write(in);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (docxOutFile != null) docxOutFile.closeEntry();
            } catch (Exception e) {
            }
        }
    }

    public void save(OutputStream output) throws Exception {
        ZipOutputStream docxOutFile = null;
        try {
            docxOutFile = new ZipOutputStream(output);
            Enumeration entriesIter = docxFile.entries();
            while (entriesIter.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entriesIter.nextElement();
                System.out.println(entry.getName());
                if (DOC_FILE_PATH.equals(entry.getName())) {
                    copyZipEntry(document.asXML().getBytes(document.getXMLEncoding()), docxOutFile, new ZipEntry(entry.getName()));
                } else {
                    copyZipEntry(docxFile.getInputStream(entry), docxOutFile, new ZipEntry(entry.getName()));
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (docxOutFile != null) docxOutFile.close();
        }
    }

    public void save(File output) throws Exception {
        OutputStream fo = null;
        try {
            save(fo = new FileOutputStream(output));
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(fo);
        }
    }

    public void saveDocumentXML(OutputStream output) throws Exception {
        Writer writer = null;
        XMLWriter xmlwriter = null;
        try {
            format.setEncoding(document.getXMLEncoding());
            writer = new OutputStreamWriter(output, document.getXMLEncoding());
            xmlwriter = new XMLWriter(writer, format);
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
        }
    }

    private Element createWrt(String content, Namespace namespace) throws Exception {
        Element el = new BaseElement("r", namespace);
        el.addElement("w:t").setText(content);
        return el;
    }

    @Override
    public void close() throws Exception {
        AutoCloseableBase.close(documentXMLIS, docxFile);
    }
}
