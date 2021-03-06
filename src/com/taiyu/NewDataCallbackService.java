package com.taiyu;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.2.1
 * 2020-11-12T16:05:15.821+08:00
 * Generated source version: 3.2.1
 * 
 */
@WebServiceClient(name = "NewDataCallbackService", 
                  wsdlLocation = "file:/C:/Users/ADMINI~1/AppData/Local/Temp/tempdir6681535978364829796.tmp/NewDataCallback_1.wsdl",
                  targetNamespace = "http://webService.taiyu.com/") 
public class NewDataCallbackService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://webService.taiyu.com/", "NewDataCallbackService");
    public final static QName NewDataCallbackPort = new QName("http://webService.taiyu.com/", "NewDataCallbackPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/ADMINI~1/AppData/Local/Temp/tempdir6681535978364829796.tmp/NewDataCallback_1.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(NewDataCallbackService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/ADMINI~1/AppData/Local/Temp/tempdir6681535978364829796.tmp/NewDataCallback_1.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public NewDataCallbackService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public NewDataCallbackService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NewDataCallbackService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public NewDataCallbackService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public NewDataCallbackService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public NewDataCallbackService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns NewDataCallback
     */
    @WebEndpoint(name = "NewDataCallbackPort")
    public NewDataCallback getNewDataCallbackPort() {
        return super.getPort(NewDataCallbackPort, NewDataCallback.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NewDataCallback
     */
    @WebEndpoint(name = "NewDataCallbackPort")
    public NewDataCallback getNewDataCallbackPort(WebServiceFeature... features) {
        return super.getPort(NewDataCallbackPort, NewDataCallback.class, features);
    }

}
