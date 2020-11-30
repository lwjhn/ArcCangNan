import com.lwjhn.domino2sql.config.DefaultConfig;
import com.lwjhn.util.AutoCloseableBase;
import com.lwjhn.util.DigestUtils;
import com.taiyu.NewDataCallback;
import com.taiyu.NewDataCallbackService;
import org.apache.commons.io.FileUtils;
import org.dom4j.tree.BaseElement;
import org.junit.Test;
import com.alibaba.fastjson.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

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
    public void testMd5() throws Exception {
        File file = new File("C:\\test\\J102-WS·2020-D10-0007\\电子收文件\\关于防控新型冠状病毒肺炎疫情应急物资费用结算工作进展情况的再次通报(办理单).pdf");
        InputStream is = new FileInputStream(file);
        try {
            org.dom4j.Element node = new BaseElement("detailinfo").addAttribute("title", "计算机文件详细信息");
            node.addElement("WJM").addAttribute("title", "计算机文件名").setText(file.getName());
            node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
            node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
            node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)));
            node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText(file.getName().replaceAll(".*\\.", ""));
            node.addElement("WJSZZY").addAttribute("title", "文件数字摘要值").setText("MD5:" + org.apache.commons.codec.digest.DigestUtils.md5Hex(is));

            System.out.println(node.asXML());//MD5:
            System.out.println(">>>--->" + DigestUtils.digest(file));

            System.out.println(">>>--->" + DigestUtils.copyFileAndDigest(file, new File("c:/test/abcd.pdf")));

        } catch (Exception e) {
            throw e;
        } finally {
            AutoCloseableBase.close(is);
        }
    }

    @Test
    public void test2() throws Exception {
        File file = new File("C:\\test\\J102-WS·2020-D10-0007.doc");
        org.dom4j.Element node = new BaseElement("detailinfo").addAttribute("title", "计算机文件详细信息");
        node.addElement("WJM").addAttribute("title", "计算机文件名").setText(file.getName());
        node.addElement("CJSJ").addAttribute("title", "计算机文件创建时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
        node.addElement("XGSJ").addAttribute("XGSJ", "计算机文件修改时间").setText(DefaultConfig.DateFormat.format(file.lastModified()));
        //node.addElement("WJDX").addAttribute("XGSJ", "计算机文件大小").setText(FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file)));
        node.addElement("GSXX").addAttribute("title", "计算机文件格式信息").setText(file.getName().replaceAll(".*\\.", ""));
        //node.addElement("WJSZZY").addAttribute("title","文件数字摘要值").setText("MD5:"+ org.apache.commons.codec.digest.DigestUtils.md5Hex(is));
        System.out.println(node.asXML());//MD5:

    }

}