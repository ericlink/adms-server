package helper.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class FileLogHelper {

    private static final Logger logger = Logger.getLogger(FileLogHelper.class.getName());
    private static FileLogHelper helper = new FileLogHelper();

    private FileLogHelper() {
    }

    public static FileLogHelper createInstance() {
        return helper;
    }

    public void log(String path, String name, byte[] contents) throws FileNotFoundException, IOException {
        File file = new File(path);
        if (file.exists()) {
            file.mkdirs();
        }

        log(path + file.separator + name, contents);
    }

    public void log(String filePathAndName, String contents) throws FileNotFoundException, IOException {
        log(filePathAndName, contents.getBytes());
    }

    /**
     * Creates a NEW file.
     * @param filePathAndName Path and file name for the NEW file.
     * @param contents Will be written to the file.
     **/
    public void log(String filePathAndName, byte[] contents) throws FileNotFoundException, IOException {
        logger.log(Level.FINE, "Logging contents (length={0}) to [{1}]", new Object[]{contents.length, filePathAndName});
        FileOutputStream fout = new FileOutputStream(filePathAndName);
        BufferedOutputStream out = new BufferedOutputStream(fout);
        try {
            out.write(contents);
            out.flush();
            out.close();
            out = null;
            fout.flush();
            fout.close();
            fout = null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
