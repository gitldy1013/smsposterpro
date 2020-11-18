package com.smsposterpro.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class ResourcesFileUtils {

    public static final String TEMP_FILE_NAME = "/temp.html";

    public static File getResourcesFile(InputStream inputStream) {
        try {
            File file = new File(TEMP_FILE_NAME);
            OutputStream os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];
            while ((len = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return file;
        } catch (IOException e) {
            log.error("异常:" + e);
        }
        return null;
    }
}
