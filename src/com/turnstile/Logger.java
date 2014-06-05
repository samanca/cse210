package com.turnstile;

import java.io.*;
import java.util.List;

public class Logger {

    static final String NEWLINE = "\r\n";
    private static Logger singleton = null;

    public static Logger SingleInstance() {
        if (singleton == null) singleton = new Logger();
        return singleton;
    }

    public String Serialize(List<String> data) {
        String retVal = "";
        for (String line : data) {
            retVal += line + NEWLINE;
        }
        return retVal;
    }

    public void Write(String filename, String log) {
        File myFile = new File(filename);
        FileOutputStream fooStream = null;
        try {
            fooStream = new FileOutputStream(myFile, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] myBytes = log.getBytes();
        
        try {
            fooStream.write(myBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fooStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
