
package com.taiyu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>saveDzgwData complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="saveDzgwData"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="data_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="data_path" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="dept_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *         &lt;element name="file_num" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveDzgwData", propOrder = {
    "dataName",
    "dataPath",
    "deptCode",
    "fileNum"
})
public class SaveDzgwData {

    @XmlElement(name = "data_name")
    protected String dataName;
    @XmlElement(name = "data_path")
    protected String dataPath;
    @XmlElement(name = "dept_code")
    protected String deptCode;
    @XmlElement(name = "file_num")
    protected String fileNum;

    /**
     * ��ȡdataName���Ե�ֵ��
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
     * ����dataName���Ե�ֵ��
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
     * ��ȡdataPath���Ե�ֵ��
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
     * ����dataPath���Ե�ֵ��
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
     * ��ȡdeptCode���Ե�ֵ��
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
     * ����deptCode���Ե�ֵ��
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
     * ��ȡfileNum���Ե�ֵ��
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
     * ����fileNum���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileNum(String value) {
        this.fileNum = value;
    }

}
