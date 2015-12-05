package helper.servlet.http;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import helper.io.FileLogHelper;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public abstract class HttpPostDocumentServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(HttpPostDocumentServlet.class.getName());
    String logFilePath = null;
    String logFileExtension = null;
    /** set this property in the subclass if a specific content type is needed on the response. **/
    protected String responseContentType = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.logFilePath = config.getInitParameter("LogFilePath");
        this.logFileExtension = config.getInitParameter("LogFileExtension");
        logger.log(Level.FINE, "LogFilePath={0}, LogFileExtension={1}", new Object[]{logFilePath, logFileExtension});

        // make the logging directory if needed
        boolean dirOk = new File(logFilePath).mkdirs();
    }

    public void destroy() {
    }

    public String getServletInfo() {
        return "HttpPostStreamHandler";
    }

    final protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        logger.warning("Illegal get attempted");
        response.sendRedirect("/"); // no gets allowed, quietly redirect them
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        byte[] responseMessage = null;
        long messageReceivedTimestampUtc = System.currentTimeMillis();
        DataInputStream is = null;
        OutputStream out = null;

        try {
            logger.fine("Read incoming post");
            is = new DataInputStream(new BufferedInputStream(request.getInputStream()));
            byte[] msg = null;
            logger.fine("Get post payload");
            try {
                is.mark(Integer.MAX_VALUE);
                logger.log(Level.FINE, "Content length={0}", request.getContentLength());
                // chunked post does not give content length so read a byte at a time
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int b = 0;
                while ((b = is.read()) > -1) {
                    baos.write(b);
                }
                msg = baos.toByteArray();
                logger.fine("Log message");
                if (this.logFilePath != null && this.logFileExtension != null) {
                    FileLogHelper.createInstance().log(this.logFilePath + System.currentTimeMillis() + "." + this.logFileExtension, msg);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception getting payload or logging message contents", e);
            } finally {
                is.reset();
            }

            // get headers to pass through to subclasses
            Map<String, String> headers = new HashMap<String, String>();
            Enumeration names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = request.getHeader(name);
                logger.log(Level.FINE, "http header name={0},value={1}", new Object[]{name, value});
                headers.put(name.toUpperCase(), value);
            }
            headers.put("REQUEST_TIME", String.valueOf(messageReceivedTimestampUtc));
            //if ( true ) throw new Exception( "TEST General Exception" );
            logger.fine("Call onMessage");
            responseMessage = onMessage(is, msg, headers);
            is.close();
            is = null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading incoming post", e);
            responseMessage = null;
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            try {
                logger.finer("Write response");
                logger.finer(new String(responseMessage));
                if (responseContentType != null) {
                    response.setContentType(responseContentType);
                }
                if (responseMessage != null) {
                    response.setContentLength(responseMessage.length);
                    out = response.getOutputStream();
                    out.write(responseMessage);
                    out.flush();
                } else {
                    response.setContentLength(0);
                }
                logger.finer("Response written");
                if (is != null) {
                    is.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                logger.log(Level.SEVERE, "Error writing response", e2);
            }
            logger.fine("Done handling post message");
        }
    }

    /**
     * This method should do whatever processing is desired on the message,
     * then create a response that will go back to the client.
     *
     * @param is Input stream (managed by base class) from the request
     *
     * @param msg the post payload as one byte[]
     *
     * @param headers http headers from request
     *
     * @return response to the client as a byte array.
     * content type for the response is set in servlet parameter.
     *
     **/
    abstract protected byte[] onMessage(DataInputStream is, byte[] msg, Map<String, String> headers)
        throws Exception;
}
