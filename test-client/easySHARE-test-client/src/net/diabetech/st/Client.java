package net.diabetech.st;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

/**
 * Confidential Information.
 * Copyright (C) 2007 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/

public class Client extends Object {
    InputStream in = null;
    DataOutputStream postOutput = null;
    OutputStream output = null;
    DataInputStream input = null;
    
    public Client() {
    }
    
    public static void main( String[] args ) {
        new Client().run();
    }
    
    public void run() {
        try {
            Properties p = new Properties();
            p.load( getClass().getClassLoader().getResourceAsStream( "conf/st.properties" ) );
            p.list( System.out );
            
            System.out.println( "get content to post" );
            in = getClass().getClassLoader().getResourceAsStream( p.getProperty( "input.file" ) );
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int value = 0;
            while(  (value = in.read()) != -1 ) {
                out.write( value );
            }
            byte[] bytes = out.toByteArray();
            postData( p, bytes );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            System.out.println( "finally" );
            try {
                if ( output != null ) { output.close(); }
                if ( postOutput != null ) { postOutput.close(); }
                if ( in != null ) { in.close(); }
                if ( input != null ) { input.close(); }
            } catch ( Exception  e ) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        
    }
    
    
    private void postData( Properties p, byte[] bytes ) throws Exception {
        //Create socket
        String hostname = p.getProperty( "server" );
        int port = Integer.valueOf( p.getProperty( "port" ) ).intValue();
        InetAddress  addr = InetAddress.getByName(hostname);
        Socket sock = new Socket(addr, port);
        
        System.out.println( "post to server" );
        System.out.println( "get output stream" );
        output = sock.getOutputStream();
        postOutput = new DataOutputStream( output );
        System.out.println( "write" );
        
        // Get and display the response data.
        input = new DataInputStream(sock.getInputStream());
        //Send header
        String path = p.getProperty( "path" );
        BufferedWriter  wr = new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
        // You can use "UTF8" for compatibility with the Microsoft virtual machine.
        wr.write("POST " + path + " HTTP/1.0\r\n");
        wr.write("Host: localhost\r\n");
        wr.write("Content-Length: " + bytes.length + "\r\n");
        wr.write("Content-Type: text/xml; charset=\"utf-8\"\r\n");
        wr.write("Authorization: Basic UTR0RlBpIXJwNlpWKW96djptIUhQcmU4V0pCQGh2Zm5K\r\n" );
        wr.write("APP_VER: " + p.getProperty( "APP_VER" ) + "\r\n");
        wr.write("IMEI: " + p.getProperty( "IMEI" ) + "\r\n");
        if ( p.getProperty( "MSG_ID" ) != null ) {
            wr.write("MSG_ID: " + p.getProperty( "MSG_ID" ) + "\r\n");
        }
        wr.write("USER-AGENT: " + p.getProperty( "USER_AGENT" ) + "\r\n");
        wr.write("\r\n");
        
        
        
        
        //Send data
        wr.write(new String(bytes));
        wr.flush();
        
        System.out.println("display");
        // Response
        BufferedReader rd = new BufferedReader(new InputStreamReader(input));
        String line;
        while((line = rd.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println( "done" );
        
        
    }
}
