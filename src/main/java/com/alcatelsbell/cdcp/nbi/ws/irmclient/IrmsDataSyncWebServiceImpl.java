
package com.alcatelsbell.cdcp.nbi.ws.irmclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "IrmsDataSyncWebServiceImpl", targetNamespace = NamingSpace.ns_default)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IrmsDataSyncWebServiceImpl {


    /**
     * 
     * @param request
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "notifyNeInfoSyncResultReturn", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com")
    @RequestWrapper(localName = "notifyNeInfoSyncResult", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com", className = "com.alcatelsbell.cdcp.nbi.ws.irmclient.NotifyNeInfoSyncResult")
    @ResponseWrapper(localName = "notifyNeInfoSyncResultResponse", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com", className = "com.alcatelsbell.cdcp.nbi.ws.irmclient.NotifyNeInfoSyncResultResponse")
    public String notifyNeInfoSyncResult(
            @WebParam(name = "request", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com")
            String request);


    /**
     *
     * @param request
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(name = "notify_sectionInfoSyncResultReturn", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com")
    @RequestWrapper(localName = "notify_sectionInfoSyncResult", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com", className = "com.alcatelsbell.cdcp.nbi.ws.irmclient.Notify_sectionInfoSyncResult")
    @ResponseWrapper(localName = "notify_sectionInfoSyncResultResponse", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com", className = "com.alcatelsbell.cdcp.nbi.ws.irmclient.Notify_sectionInfoSyncResultResponse")
    public String notify_sectionInfoSyncResult(
            @WebParam(name = "request", targetNamespace = "http://impl.service.ics.webservice.integration.irm.com")
            String request);

}
