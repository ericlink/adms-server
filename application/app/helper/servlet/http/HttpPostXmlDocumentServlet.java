package helper.servlet.http;

import java.io.DataInputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class HttpPostXmlDocumentServlet extends HttpPostDocumentServlet implements ErrorHandler {

    private static final Logger logger = Logger.getLogger(HttpPostXmlDocumentServlet.class.getName());
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    public HttpPostXmlDocumentServlet() {
        super.responseContentType = "text/xml";
    }

    /**
     * This method should do whatever processing is desired on the message, then create a response that will go back to the client.
     *
     * @param messagse The message payload that was posted to the web server is passed in as a byte[].
     *
     * @return response to the client as a byte array.  content type for the response is set in servlet parameter.
     **/
    protected byte[] onMessage(DataInputStream is, byte[] msg, Map<String, String> headers)
        throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(this);
        Document xmlMessage = db.parse(is);

        //if ( true ) throw new Exception( "TEST General exception" );
        logger.fine("Parsed xml message " + xmlMessage.getDocumentElement().getTagName());

        return onMessage(xmlMessage);
    }

    protected byte[] onError(Exception e) {
        return e.getMessage().getBytes();
    }

    /**
     * Process message and produce a response in this method.
     * Default implementation just returns posted message.
     *
     * @param messages The message payload that was posted to the web server is passed in as a parsed Xml Document.
     *
     * @return response to the client as a byte array.  content type for the response is set in servlet parameter.
     **/
    protected byte[] onMessage(Document message) {
        return message.toString().getBytes();
    }

    private void saxError(SAXParseException e) {
        logger.log(Level.FINE, "SAX: Error in document {0}@{1}:{2}", new Object[]{e.getPublicId(), e.getColumnNumber(), e.getLineNumber()});
    }

    public void error(SAXParseException e) throws SAXException {
        saxError(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        saxError(e);
    }

    public void warning(SAXParseException e) throws SAXException {
        saxError(e);
    }
}
