package com.smsposterpro.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class ResourcesFileUtils {

    public static final String TEMP_FILE_NAME = "temp.html";
    public static final String TEMP_EXP_FILE_NAME = "结果导出.html";
    public static final String TEMP_FILE_DIR = "temp";

    public static File getResourcesFile(InputStream inputStream, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int len;
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

    /**
     * 获取最大相同字串
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String findMaxSubString(String str1, String str2) {
        String result = "";

        String mixStr = str1.length() < str2.length() ? str1 : str2;
        String maxStr = str1.length() > str2.length() ? str1 : str2;
        //外循环控制从左到右的下标，内循环控制从右到左的下标
        for (int i = 0; i < mixStr.length(); i++) {
            for (int j = mixStr.length(); j >= i; j--) {
                String str = mixStr.substring(i, j);
                //判断当前子串是否为公共子串
                if (maxStr.contains(str)) {
                    //找出最大相同子串
                    if (result.length() < str.length()) {
                        result = str;
                    }
                }
            }
        }
        if (result.length() > 1 && str1.endsWith(result) && str2.startsWith(result)) {
            result = str1.replaceAll(result, "") + result + str2.replaceAll(result, "");
        } else {
            result = str1.endsWith("/") ? (str1 + str2) : (str1 + "/" + str2);
        }
        return result;
    }

}
