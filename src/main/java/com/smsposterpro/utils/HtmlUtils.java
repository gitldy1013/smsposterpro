package com.smsposterpro.utils;

import com.smsposterpro.exception.AesException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class HtmlUtils {
    /* 使用jsoup解析html并转化为提取字符串*/
    public static boolean html2Str(String html, String flag, StringBuilder sb, String reg) throws AesException {
        if (StringUtils.isBlank(reg)) {
            reg = "span";
        }
        Document doc = Jsoup.parse(html);
        try {
            Elements span = doc.select(reg);
            boolean check = StringUtils.isNotBlank(flag) && span.size() > 0 && span.get(0).text().equals(flag) || StringUtils.isBlank(flag);
            if (check) {
                Iterator<Element> iterator = span.iterator();
                StringBuilder sbsub = new StringBuilder();
                while (iterator.hasNext()) {
                    Element next = iterator.next();
                    String text = next.text();
                    sbsub.append(text).append(System.getProperty("line.separator"));
                }
                sb.append(sbsub);
            }
            return check;
        } catch (Exception e) {
            throw new AesException(AesException.errorReg);
        }
    }

    public static String readHtmlFileByPath(String path, String flag) {
        return readHtmlFileByPath(path, flag, null);
    }

    public static String readHtmlFileByPath(String path, String flag, String reg) {
        File file = new File(path);
        return readHtmlFile(file, flag, reg);
    }

    public static String readHtmlFile(File file, String flag) {
        return readHtmlFile(file, flag, null);
    }

    public static String readHtmlFile(String reg, File file) {
        return readHtmlFile(file, null, reg);
    }

    public static String readHtmlFile(File file) {
        return readHtmlFile(file, null, null);
    }

    public static String readHtmlFile(File file, String flag, String reg) {
        FileReader fr = null;
        try {
            StringBuilder sb = new StringBuilder();
            fr = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(fr);
            String instring;
            while ((instring = bufferedreader.readLine()) != null) {
                if (html2Str(instring.trim(), flag, sb, reg)) {
                    flag = null;
                }
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("读取数据异常", e);
            return "读取数据异常";
        } catch (AesException e) {
            return e.getMessage();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                log.error("数据流关闭异常", e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(readHtmlFileByPath("C:\\Users\\liudongyang\\Documents\\Axure\\HTML\\11160617-合作_recovered_recovered_recovered\\编辑.html", "保存"));
        //一级目录a,二级目录b,三级目录c
        String[] directories = {"a", "b", "c"};
        String rootName = new File(".").getAbsolutePath();
        File file = createFileWithMultilevelDirectory(directories, "test_file.txt", rootName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write("创建带有多级目录文件示例".getBytes());
    }

    /**
     * 创建多层目录结构文件
     */
    public static File createFileWithMultilevelDirectory(String[] directories, String fileName, String rootName) {
        //调用上面的创建多级目录的方法
        File filePath = createMultilevelDirectory(directories, rootName);
        File file = new File(filePath, fileName);
        boolean b = false;
        try {
            b = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("创建文件目录异常", e);
        }
        assert b;
        return file;
    }

    //创建多级目录
    public static File createMultilevelDirectory(String[] directories, String rootPath) {
        if (directories.length == 0) {
            return null;
        }
        File root = new File(rootPath);
        for (String s : directories) {
            File directory = new File(root, s);
            boolean mkdir = directory.mkdir();
            assert mkdir;
            root = directory;
        }
        return root;
    }
}
