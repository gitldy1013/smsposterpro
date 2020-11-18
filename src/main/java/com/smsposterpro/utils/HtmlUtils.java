package com.smsposterpro.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class HtmlUtils {
    /* 使用jsoup解析html并转化为提取字符串*/
    public static boolean html2Str(String html, String flag, StringBuilder sb) {
        Document doc = Jsoup.parse(html);
        Elements span = doc.select("span");
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
    }

    public static String readHtmlFileByPath(String path, String flag) {
        File file = new File(path);
        return readHtmlFile(file, flag);
    }

    public static String readHtmlFile(File file, String flag) {
        FileReader fr = null;
        try {
            StringBuilder sb = new StringBuilder();
            fr = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(fr);
            String instring;
            while ((instring = bufferedreader.readLine()) != null) {
                if (html2Str(instring.trim(), flag, sb)) {
                    flag = null;
                }
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("读取数据异常", e);
            return "读取数据异常";
        } finally {
            try {
                assert fr != null;
                fr.close();
            } catch (IOException e) {
                log.error("数据流关闭异常", e);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(readHtmlFileByPath("C:\\Users\\liudongyang\\Documents\\Axure\\HTML\\11160617-合作_recovered_recovered_recovered\\编辑.html", "保存"));
    }

}
