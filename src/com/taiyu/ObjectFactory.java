
package com.taiyu;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.taiyu package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DataImportXmlZip_QNAME = new QName("http://webService.taiyu.com/", "dataImportXml_zip");
    private final static QName _DataImportXmlZipResponse_QNAME = new QName("http://webService.taiyu.com/", "dataImportXml_zipResponse");
    private final static QName _NewDataCallback_QNAME = new QName("http://webService.taiyu.com/", "newDataCallback");
    private final static QName _NewDataCallbackResponse_QNAME = new QName("http://webService.taiyu.com/", "newDataCallbackResponse");
    private final static QName _SaveDzgwData_QNAME = new QName("http://webService.taiyu.com/", "saveDzgwData");
    private final static QName _SaveDzgwDataResponse_QNAME = new QName("http://webService.taiyu.com/", "saveDzgwDataResponse");
    private final static QName _SaveZjgData_QNAME = new QName("http://webService.taiyu.com/", "saveZjgData");
    private final static QName _SaveZjgDataResponse_QNAME = new QName("http://webService.taiyu.com/", "saveZjgDataResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.taiyu
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataImportXmlZip }
     * 
     */
    public DataImportXmlZip createDataImportXmlZip() {
        return new DataImportXmlZip();
    }

    /**
     * Create an instance of {@link DataImportXmlZipResponse }
     * 
     */
    public DataImportXmlZipResponse createDataImportXmlZipResponse() {
        return new DataImportXmlZipResponse();
    }

    /**
     * Create an instance of {@link NewDataCallback_Type }
     * 
     */
    public NewDataCallback_Type createNewDataCallback_Type() {
        return new NewDataCallback_Type();
    }

    /**
     * Create an instance of {@link NewDataCallbackResponse }
     * 
     */
    public NewDataCallbackResponse createNewDataCallbackResponse() {
        return new NewDataCallbackResponse();
    }

    /**
     * Create an instance of {@link SaveDzgwData }
     * 
     */
    public SaveDzgwData createSaveDzgwData() {
        return new SaveDzgwData();
    }

    /**
     * Create an instance of {@link SaveDzgwDataResponse }
     * 
     */
    public SaveDzgwDataResponse createSaveDzgwDataResponse() {
        return new SaveDzgwDataResponse();
    }

    /**
     * Create an instance of {@link SaveZjgData }
     * 
     */
    public SaveZjgData createSaveZjgData() {
        return new SaveZjgData();
    }

    /**
     * Create an instance of {@link SaveZjgDataResponse }
     * 
     */
    public SaveZjgDataResponse createSaveZjgDataResponse() {
        return new SaveZjgDataResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataImportXmlZip }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "dataImportXml_zip")
    public JAXBElement<DataImportXmlZip> createDataImportXmlZip(DataImportXmlZip value) {
        return new JAXBElement<DataImportXmlZip>(_DataImportXmlZip_QNAME, DataImportXmlZip.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataImportXmlZipResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "dataImportXml_zipResponse")
    public JAXBElement<DataImportXmlZipResponse> createDataImportXmlZipResponse(DataImportXmlZipResponse value) {
        return new JAXBElement<DataImportXmlZipResponse>(_DataImportXmlZipResponse_QNAME, DataImportXmlZipResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewDataCallback_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "newDataCallback")
    public JAXBElement<NewDataCallback_Type> createNewDataCallback(NewDataCallback_Type value) {
        return new JAXBElement<NewDataCallback_Type>(_NewDataCallback_QNAME, NewDataCallback_Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NewDataCallbackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "newDataCallbackResponse")
    public JAXBElement<NewDataCallbackResponse> createNewDataCallbackResponse(NewDataCallbackResponse value) {
        return new JAXBElement<NewDataCallbackResponse>(_NewDataCallbackResponse_QNAME, NewDataCallbackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveDzgwData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "saveDzgwData")
    public JAXBElement<SaveDzgwData> createSaveDzgwData(SaveDzgwData value) {
        return new JAXBElement<SaveDzgwData>(_SaveDzgwData_QNAME, SaveDzgwData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveDzgwDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "saveDzgwDataResponse")
    public JAXBElement<SaveDzgwDataResponse> createSaveDzgwDataResponse(SaveDzgwDataResponse value) {
        return new JAXBElement<SaveDzgwDataResponse>(_SaveDzgwDataResponse_QNAME, SaveDzgwDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveZjgData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "saveZjgData")
    public JAXBElement<SaveZjgData> createSaveZjgData(SaveZjgData value) {
        return new JAXBElement<SaveZjgData>(_SaveZjgData_QNAME, SaveZjgData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveZjgDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webService.taiyu.com/", name = "saveZjgDataResponse")
    public JAXBElement<SaveZjgDataResponse> createSaveZjgDataResponse(SaveZjgDataResponse value) {
        return new JAXBElement<SaveZjgDataResponse>(_SaveZjgDataResponse_QNAME, SaveZjgDataResponse.class, null, value);
    }

}
