import com.lwjhn.cangnan.AttachForm;
import com.lwjhn.cangnan.AttachNormal;
import com.lwjhn.cangnan.ModuleType;
import com.lwjhn.domino2sql.config.DefaultConfig;
import com.lwjhn.util.AutoCloseableBase;
import com.lwjhn.util.DigestUtils;
import com.lwjhn.util.FileOperator;
import com.lwjhn.util.XmlBase;
import com.taiyu.NewDataCallback;
import com.taiyu.NewDataCallbackService;
import org.apache.commons.io.FileUtils;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultAttribute;
import org.junit.Test;
import com.alibaba.fastjson.*;
import sun.net.TelnetOutputStream;
import sun.security.krb5.internal.ccache.CCacheOutputStream;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: lwjhn
 * @Date: 2020-11-11
 * @Description: PACKAGE_NAME
 * @Version: 1.0
 */
public class Demo {
    @Test
    public void test() throws Exception {
        URL url = new URL("http://121.41.61.89:8081/ty/webService/NewDataCallback?wsdl");
        NewDataCallbackService src = new NewDataCallbackService(url);
        NewDataCallback arc = src.getNewDataCallbackPort();
        String res = arc.saveDzgwData("J102-WS·2020-D10-0007", "http://218.5.2.252:3380/downloadFile/J102-WS·2020-D10-0007.zip", "J001", "发文");
        JSONObject jres = JSON.parseObject(res);
        System.out.println(jres.getBoolean("flag"));
        System.out.println(jres.getString("msg"));
    }

    @Test
    public void test4() throws Exception {
        System.out.println(Pattern.compile("^Notice$", Pattern.CASE_INSENSITIVE).matcher("notice").lookingAt());
        System.out.println(Pattern.compile("^Notice$", Pattern.CASE_INSENSITIVE).matcher("notice").matches());
        System.out.println(FileOperator.getFileAliasByRegex("ads\\2212.pdf"));
    }

    @Test
    public void test1() throws Exception {
        FileOutputStream fo = null;
        Writer writer = null;
        try {
            fo = new FileOutputStream("c:/test/abcd.txt");
            writer = new OutputStreamWriter(fo, "UTF-8");
            writer.write("abcd");
            writer.write("111111");
            writer.flush();

            writer = new OutputStreamWriter(fo, "UTF-8");
            writer.write("33333");
            writer.write("44444");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(writer, fo);
        }

        System.out.println(FileOperator.getFileNameByRegex("/aid/sdk\\sdf/111.pdf"));
        System.out.println(FileOperator.getFileFolderByRegex("/aid/sdk\\sdf/111.pdf"));
        System.out.println(FileOperator.getAvailablePath(false, "", "1111:333>444\\\\7777.88///999/", "aaa//bb/ccc", "/uuuuu///kkk/"));
    }

    @Test
    public void testMd5() throws Exception {
        File file = new File("C:\\test\\J102-WS·2020-D10-0007\\电子收文件\\关于防控新型冠状病毒肺炎疫情应急物资费用结算工作进展情况的再次通报(办理单).pdf");
        InputStream is = new FileInputStream(file);
        try {
            Element node = new BaseElement("detailinfo").addAttribute("title", "计算机文件详细信息");
            node.addElement("WJM").addAttribute("title", "计算机文件名").setText(file.getName());
            node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
            node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
            node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)));
            node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText(file.getName().replaceAll(".*\\.", ""));
            node.addElement("WJSZZY").addAttribute("title", "文件数字摘要值").setText("MD5:" + org.apache.commons.codec.digest.DigestUtils.md5Hex(is));

            Element node2 = (Element) node.clone();
            node2.setAttributes(new ArrayList() {{
                add(new DefaultAttribute("title", "测试"));
                add(new DefaultAttribute("id", "idabcd"));
            }});

            System.out.println(node.asXML());//MD5:
            System.out.println(">>>--->" + DigestUtils.digest(file));
            System.out.println(">>>--->" + DigestUtils.copyFileAndDigest(file, new File("c:/test/abcd.pdf")));
            System.out.println("\n\n\n>>>--node2->");
            System.out.println(node2.asXML());
        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    public static final Pattern PATTERN_EXT = Pattern.compile("\\.([0-9a-z]+$)", Pattern.CASE_INSENSITIVE);

    @Test
    public void test2() throws Exception {
        ModuleType moduleType = ModuleType.DISPATCH;
        int i = 1;
        String Form = AttachForm.PROCESSING.toString();
        System.out.println(Form);
        System.out.println(moduleType.getTypes()
                .get(AttachForm.enumValueOfQuiet(Form))
                .get(i < 0 ? AttachNormal.UNNORMAL : AttachNormal.NORMAL).getAlias());
    }

}