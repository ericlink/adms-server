package helper.net;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006, 2007 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class HttpClientHelper {

    private static Logger logger = Logger.getLogger(HttpClientHelper.class.getName());

    private HttpClientHelper() {
    }

    static public byte[] postRequest(URL url, byte[] message) throws Exception {
        OutputStream output = null;
        DataOutputStream postOutput = null;
        DataInputStream response = null;

        try {
            logger.fine("Open connection to server " + url.toString());

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);

            // these three request properties are the only XML specific items:
            urlConn.setRequestProperty("Content-Type", "text/xml");
            urlConn.setRequestProperty("Accept", "text/html");
            urlConn.setRequestProperty("Accept", "text/plain");


            logger.fine("Get output stream");
            output = urlConn.getOutputStream();
            postOutput = new DataOutputStream(output);

            logger.fine("Write data to server");
            postOutput.write(message);
            postOutput.flush();
            postOutput.close();
            output.flush();
            output.close();

            postOutput = null;
            output = null;

            logger.fine("Get response");
            response = new DataInputStream(new BufferedInputStream(urlConn.getInputStream()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int value;
            while (-1 != ((value = response.read()))) {
                baos.write(value);
            }

            response.close();
            response = null;

            return baos.toByteArray();

        } finally {
            logger.fine("Finally; close streams if needed");
            try {
                // if any of these are not null, something went wrong in normal flow
                if (output != null) {
                    output.close();
                }
                if (postOutput != null) {
                    postOutput.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception closing streams", e);
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Socket based post works correctly w/ jdk 1.5
     **/
    static public byte[] postRequest(String hostname, int port, String path, byte[] message) throws Exception {
        OutputStream output = null;
        DataOutputStream postOutput = null;
        DataInputStream input = null;
        Socket sock = null;
        BufferedWriter wr = null;
        BufferedReader rd = null;
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            sock = new Socket(addr, port);
            logger.log(Level.FINE, "Open connection to server {0}", sock.getInetAddress().toString());
            output = sock.getOutputStream();
            postOutput = new DataOutputStream(output);
            input = new DataInputStream(sock.getInputStream());
            wr = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
            // You can use "UTF8" for compatibility with the Microsoft virtual machine.
            wr.write("POST " + path + " HTTP/1.0\r\n");
            wr.write("Host: localhost\r\n");
            wr.write("Content-Length: " + message.length + "\r\n");
            wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
            wr.write("\r\n");
            wr.write(new String(message));
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(input));
            String line;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((line = rd.readLine()) != null) {
                baos.write(line.getBytes());
            }
            return baos.toByteArray();
        } finally {
            logger.fine("Finally; close streams if needed");
            try {
                // if any of these are not null, something went wrong in normal flow
                if (output != null) {
                    output.close();
                }
                if (postOutput != null) {
                    postOutput.close();
                }
                if (input != null) {
                    input.close();
                }
                if (sock != null) {
                    sock.close();
                }
                if (wr != null) {
                    wr.close();
                }
                if (rd != null) {
                    rd.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception closing streams", e);
                e.printStackTrace();
                return null;
            }
        }

    }

    static public byte[] getResource(String hostname, int port, String path, byte[] message) throws Exception {
        OutputStream output = null;
        DataOutputStream postOutput = null;
        DataInputStream input = null;
        Socket sock = null;
        BufferedWriter wr = null;
        BufferedReader rd = null;
        int length = 0;
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            sock = new Socket(addr, port);
            logger.fine("Open connection to server {0}" + sock.getInetAddress().toString());
            output = sock.getOutputStream();
            postOutput = new DataOutputStream(output);
            input = new DataInputStream(sock.getInputStream());
            wr = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
            // You can use "UTF8" for compatibility with the Microsoft virtual machine.
            wr.write("GET " + path + " HTTP/1.0\r\n");
            wr.write("Host: localhost\r\n");
            if (message != null) {
                length = message.length;
            }
            wr.flush();
            rd = new BufferedReader(new InputStreamReader(input));
            String line;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((line = rd.readLine()) != null) {
                baos.write(line.getBytes());
            }
            return baos.toByteArray();
        } finally {
            logger.fine("Finally; close streams if needed");
            try {
                // if any of these are not null, something went wrong in normal flow
                if (output != null) {
                    output.close();
                }
                if (postOutput != null) {
                    postOutput.close();
                }
                if (input != null) {
                    input.close();
                }
                if (sock != null) {
                    sock.close();
                }
                if (wr != null) {
                    wr.close();
                }
                if (rd != null) {
                    rd.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception closing streams", e);
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("usage: net.diabetech.net.URLConnectionHelper url inputFileName");
            System.exit(0);
        }

        String url = args[0];
        String inputFileName = args[1];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(inputFileName);
        int b;
        while (-1 != (b = in.read())) {
            baos.write(b);
        }

        byte[] response = postRequest(new URL(url), baos.toByteArray());

        for (int i = 0; i < response.length; i++) {
            System.out.print((char) response[i]);
        }

    }
}
