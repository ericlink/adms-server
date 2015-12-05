package helper.activation;

import java.io.*;
import javax.activation.*;

/**
 * .
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * $CVSHeader$ $Name$
 **/
public class ByteArrayDataSource implements DataSource, Serializable {

    private byte[] data;	// data
    private String contentType;	// content-type
    private String name;

    /* Create a DataSource from an input stream */
    public ByteArrayDataSource(InputStream is, String contentType, String name) {
        this.contentType = contentType;
        this.name = name;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;

            while ((ch = is.read()) != -1) {
                os.write(ch);
            }
            data = os.toByteArray();
        } catch (IOException ioex) {
        }
    }

    /* Create a DataSource from a byte array */
    public ByteArrayDataSource(byte[] data, String contentType, String name) {
        this.data = data;
        this.contentType = contentType;
        this.name = name;
    }

    /* Create a DataSource from a String */
    public ByteArrayDataSource(String data, String contentType, String name) {
        this.contentType = contentType;
        this.name = name;
        try {
            // Assumption that the string contains only ASCII
            // characters!  Otherwise just pass a charset into this
            // constructor and use it in getBytes()
            this.data = data.getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException uex) {
        }
    }

    /**
     * Return an InputStream for the data.
     * Note - a new stream must be returned each time.
     */
    public InputStream getInputStream() throws IOException {
        if (data == null) {
            throw new IOException("no data");
        }
        return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot do this");
    }

    public String getContentType() {
        return contentType;
    }

    public String getName() {
        return name;
    }

    /**
     * Convenience method to get bytes directly, 
     * if you know what kind of data source this is, 
     * you don't need to get the stream, just grab the bytes.
     ***/
    public byte[] getBytes() {
        return data;
    }
}
