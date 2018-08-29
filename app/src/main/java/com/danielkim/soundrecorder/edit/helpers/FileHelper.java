package com.danielkim.soundrecorder.edit.helpers;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static String setupFile(String fileName, String folderPath, String extention) throws IOException {
        String filePathFull = folderPath;
        File file = new File(filePathFull);
        if(!file.exists()){
            file.mkdirs();
        }
        filePathFull += File.separator + fileName + extention;
        file = new File(filePathFull);
        if(!file.exists()){
            file.createNewFile();
        }
        file.setWritable(true);
        file.setReadable(true);

        return filePathFull;
    }
}
