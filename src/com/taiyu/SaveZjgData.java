
package com.taiyu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>saveZjgData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="saveZjgData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="data_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="data_path" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="dept_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="file_num" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="b64SignedData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="b64SignCert" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="jslx" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="hdjklj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveZjgData", propOrder = {
    "dataName",
    "dataPath",
    "deptCode",
    "fileNum",
    "b64SignedData",
    "b64SignCert",
    "jslx",
    "hdjklj"
})
public class SaveZjgData {

    @XmlElement(name = "data_name")
    protected String dataName;
    @XmlElement(name = "data_path")
    protected String dataPath;
    @XmlElement(name = "dept_code")
    protected String deptCode;
    @XmlElement(name = "file_num")
    protected String fileNum;
    protected String b64SignedData;
    protected String b64SignCert;
    protected String jslx;
    protected String hdjklj;

    /**
     * 获取dataName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * 设置dataName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataName(String value) {
        this.dataName = value;
    }

    /**
     * 获取dataPath属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataPath() {
        return dataPath;
    }

    /**
     * 设置dataPath属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataPath(String value) {
        this.dataPath = value;
    }

    /**
     * 获取deptCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * 设置deptCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeptCode(String value) {
        this.deptCode = value;
    }

    /**
     * 获取fileNum属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileNum() {
        return fileNum;
    }

    /**
     * 设置fileNum属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileNum(String value) {
        this.fileNum = value;
    }

    /**
     * 获取b64SignedData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getB64SignedData() {
        return b64SignedData;
    }

    /**
     * 设置b64SignedData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setB64SignedData(String value) {
        this.b64SignedData = value;
    }

    /**
     * 获取b64SignCert属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getB64SignCert() {
        return b64SignCert;
    }

    /**
     * 设置b64SignCert属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setB64SignCert(String value) {
        this.b64SignCert = value;
    }

    /**
     * 获取jslx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJslx() {
        return jslx;
    }

    /**
     * 设置jslx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJslx(String value) {
        this.jslx = value;
    }

    /**
     * 获取hdjklj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHdjklj() {
        return hdjklj;
    }

    /**
     * 设置hdjklj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHdjklj(String value) {
        this.hdjklj = value;
    }

}
