package com.smsposterpro.utils;

import com.smsposterpro.exception.AesException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_EXP_FILE_NAME;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_DIR;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_NAME;
import static java.util.regex.Pattern.compile;

@Slf4j
public class HtmlUtils {
    /* 使用jsoup解析html并转化为提取字符串*/
    public static String html2Str(String html, String flag, String reg, String attr) throws AesException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isBlank(reg)) {
            reg = "span";
        }
        Document doc = Jsoup.parse(html);
        try {
            boolean check = false;
            Elements span = doc.select(reg);
            Iterator<Element> iterator = span.iterator();
            StringBuilder sbsub = new StringBuilder();
            while (iterator.hasNext()) {
                Element next = iterator.next();
                String value;
                if (StringUtils.isNotEmpty(attr)) {
                    value = next.attr(attr).trim();
                } else {
                    value = next.text().trim();
                }
                if ((check || StringUtils.isBlank(flag) || (StringUtils.isNotBlank(flag) && value.equals(flag))) && StringUtils.isNotEmpty(value)) {
                    sbsub.append(value).append(System.getProperty("line.separator"));
                    check = true;
                }
            }
            if (StringUtils.isNotBlank(sbsub)) {
                sb.append(sbsub);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new AesException(AesException.errorReg);
        }
    }

    public static String readHtmlFileByPath(String path, String flag) {
        return readHtmlFileByPath(path, flag, null);
    }

    public static String readHtmlFileByPath(String path, String flag, String reg) {
        File file = new File(path);
        return readHtmlFile(file, flag, reg, null);
    }

    public static String readHtmlFile(File file, String flag) {
        return readHtmlFile(file, flag, null, null);
    }

    public static String readHtmlFile(String reg, File file) {
        String selector = null;
        String attr = null;
        if (StringUtils.isNotEmpty(reg)) {
            String[] split = reg.split("\\|");
            if (split.length > 2) {
                return "<h2>索引条件参数有误,请将条件选择器和属性选择器用\\|线分隔。</h2>";
            } else if (split.length == 2) {
                selector = split[0].trim();
                attr = split[1].trim();
            } else if (split.length == 1) {
                selector = split[0].trim();
            }
        }
        return readHtmlFile(file, null, selector, attr);
    }

    public static String readHtmlFile(File file) {
        return readHtmlFile(file, null, null, null);
    }

    public static String readHtmlFile(File file, String flag, String reg, String attr) {
        FileReader fr = null;
        try {
            StringBuilder subSb = new StringBuilder();
            fr = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(fr);
            String instring;
            while ((instring = bufferedreader.readLine()) != null) {
                subSb.append(instring.trim());
            }
            return html2Str(subSb.toString().trim(), flag, reg, attr);
        } catch (IOException e) {
            log.error("读取数据异常", e);
            return "<h2>读取数据异常,当前还未上传有效文件！<h2>";
        } catch (AesException e) {
            return "<h2>" + e.getMessage() + "</h2>";
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

    public static boolean doSaveTempFile(String IPStr, String content) {
        return doSaveFile(IPStr, content, null, TEMP_FILE_NAME);
    }

    public static boolean doSaveFile(String IPStr, String content, String doMain, String FileName) {
        FileWriter fw = null;
        String[] directories;
        try {
            if (StringUtils.isNotEmpty(doMain)) {
                if (doMain.contains("/")) {
                    String[] doMains = doMain.split("/");
                    directories = ArrayUtils.addAll(new String[]{TEMP_FILE_DIR, IPStr}, doMains);
                } else {
                    directories = new String[]{TEMP_FILE_DIR, IPStr, doMain};
                }
            } else {
                directories = new String[]{TEMP_FILE_DIR, IPStr};
            }
            String rootName = new File(".").getAbsolutePath();
            File tempFile = createFileWithMultilevelDirectory(directories, FileName, rootName);
            fw = new FileWriter(tempFile);
            fw.write(content);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("爬取网页信息异常", e);
            return true;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("关闭文件流失败", e);
                }
            }
        }
        return false;
    }

    public static boolean doSaveImgFile(String IPStr, byte[] body, String doMain, String fileName) {
        FileOutputStream fw = null;
        String[] directories;
        try {
            if (StringUtils.isNotEmpty(doMain)) {
                if (doMain.contains("/")) {
                    String[] doMains = doMain.split("/");
                    directories = ArrayUtils.addAll(new String[]{TEMP_FILE_DIR, IPStr}, doMains);
                } else {
                    directories = new String[]{TEMP_FILE_DIR, IPStr, doMain};
                }
            } else {
                directories = new String[]{TEMP_FILE_DIR, IPStr};
            }
            String rootName = new File(".").getAbsolutePath();
            File tempFile = createFileWithMultilevelDirectory(directories, fileName, rootName);
            fw = new FileOutputStream(tempFile);
            fw.write(body);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.info("爬取网页信息异常", e);
            return true;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("关闭文件流失败", e);
                }
            }
        }
        return false;
    }

    public static String getRes(String param, String IPStr, String doAct) {
        String htmlFile = null;
        param = org.springframework.util.StringUtils.isEmpty(param) ? null : param;
        File file = null;
        try {
            file = new File("./" + TEMP_FILE_DIR + "/" + IPStr + "/" + TEMP_FILE_NAME);
            log.info("获取到的文件路径：{}", file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("文件还未上传！");
            htmlFile = "<h2>文件还未上传！首次调用需要上传文件</h2>";
        }
        if (htmlFile == null) {
            if ("filter".equals(doAct)) {
                htmlFile = HtmlUtils.readHtmlFile(file, param);
            } else if ("regcus".equals(doAct)) {
                htmlFile = HtmlUtils.readHtmlFile(param, file);
            } else {
                htmlFile = HtmlUtils.readHtmlFile(file);
            }
        }
        log.info(htmlFile);
        if (org.springframework.util.StringUtils.isEmpty(htmlFile)) {
            htmlFile = "<h2>检查一下检索内容吧~没有找到符合的过滤数据呢!</h2>";
        }
        try {
            FileCopyUtils.copy(htmlFile, new FileWriter(new File("./" + TEMP_FILE_DIR + "/" + IPStr + "/" + TEMP_EXP_FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件保存出错");
        }
        return htmlFile;
    }

    public static boolean regUrl(String param) {
        String regex = "(https?|HTTP)://[-\\w+&@#/%=~|?!:,.;]+[-\\w+&@#/%=~|]";
        Pattern pattern = Pattern.compile(regex);
        return org.springframework.util.StringUtils.isEmpty(param) || !pattern.matcher(param).matches();
    }

    //爬取一个域名下所有url的文件内容
    public static void getArticleURLs(String IPStr, String param, Set<String> hrefs, String local) {
        if (!regUrl(param)) {
            try {
                Document document = Jsoup.connect(param).get();
                URL url = new URL(param);
                String orgin = url.getProtocol() + "://" + url.getHost() + ((url.getPort() > 0) ? ":" + url.getPort() : "");
                String protocol = url.getProtocol() + ":";
                String domain = url.getHost().replaceAll("\\.", "");
                Elements select = document.select("a");
                Elements link = document.select("link");
                Elements script = document.select("script");
                Elements img = document.select("img");
                Elements source = document.select("source");
                //爬取css和js文件
                //downloadByAttr(IPStr, hrefs, orgin, protocol, domain, link, "href");
                //downloadByAttr(IPStr, hrefs, orgin, protocol, domain, script, "src");
                //img
                //downloadByAttr(IPStr, hrefs, orgin, protocol, domain, link, "href");
                //downloadByAttr(IPStr, hrefs, orgin, protocol, domain, img, "src");
                //downloadByAttr(IPStr, hrefs, orgin, protocol, domain, img, "data-original");
                //下载视频
                downloadByAttr(IPStr, hrefs, orgin, protocol, domain, script, "type");
                downloadByAttr(IPStr, hrefs, orgin, protocol, domain, source, "src");
                //递归爬取静态页面
                for (Element sh : select) {
                    String href = sh.attr("href");
                    if (!href.startsWith("http")) {
                        if (!href.contains("javascript")) {
                            href = orgin + href.replaceAll("^(\\.)*", "");
                        } else {
                            continue;
                        }
                    }
                    String resPathNoParam = getResPathNoParam(href);
                    if (!regUrl(href) && href.startsWith(orgin + local) && !orgin.equals(resPathNoParam)) {
                        try {
                            repyHtml(IPStr, hrefs, sh, resPathNoParam, local);
                        } catch (Exception e) {
                            try {
                                String s = protocol + getResPathNoParam(sh.attr("href"));
                                repyHtml(IPStr, hrefs, sh, s, local);
                            } catch (Exception ex) {
                                log.error("爬取当前页面异常:{}", ex.getMessage(), e);
                            }
                        }
                    } else {
                        sh.attr("href", href);
                    }
                }
                //log.info("保存html：" + param);
                //String s = domain + param.replace(orgin, "");
                //String hrefPath = s.substring(0, s.lastIndexOf("/"));
                //doSaveFile(IPStr, document.toString(), hrefPath, getResName(s));
            } catch (Exception e) {
                log.error("爬取当前页面异常:{}", e.getMessage(), e);
            }
        }
    }

    private static void repyHtml(String IPStr, Set<String> hrefs, Element sh, String href, String local) {
        if (!hrefs.contains(href)) {
            hrefs.add(href);
            log.info("路径：{}；数量：{}。", href, hrefs.size());
            getArticleURLs(IPStr, href, hrefs, local);
        }
        sh.attr("href", href);
    }

    private static void downloadByAttr(String IPStr, Set<String> hrefs, String orgin, String protocol, String domain, Elements elements, String attr) {
        for (Element el : elements) {
            String href = el.attr(attr);
            if (StringUtils.isNotEmpty(href)) {
                if (href.contains(".m3u8")) {
                    pyM3U8(IPStr, hrefs, domain, el, href);
                    continue;
                }
                if (el.html().trim().contains(".m3u8")) {
                    String s = el.html();
                    pyM3U8(IPStr, hrefs, domain, el, s);
                    continue;
                }
                if (!href.startsWith("http")) {
                    String trimHref = href.replaceAll("^(\\.)*", "");
                    if (trimHref.startsWith("/")) {
                        try {
                            pySources(IPStr, hrefs, domain, attr, el, href, orgin + trimHref, getResPathDir(href));
                        } catch (HttpStatusException e) {
                            try {
                                pySources(IPStr, hrefs, domain, attr, el, href, protocol + trimHref, getResPathDir(href));
                            } catch (HttpStatusException es) {
                                log.error("爬取当前页面异常:{}", e.getStatusCode(), e);
                            } catch (Exception ex) {
                                log.error("爬取当前页面异常:{}", ex.getMessage(), ex);
                            }
                        } catch (Exception e) {
                            log.error("爬取当前页面异常:{}", e.getMessage(), e);
                        }
                    }
                } else {
                    try {
                        pySources(IPStr, hrefs, domain, attr, el, href, getResPathNoParam(href), "/webimgs");
                    } catch (HttpStatusException e) {
                        log.error("爬取当前页面异常:{}", e.getStatusCode(), e);
                    } catch (Exception e) {
                        log.error("爬取当前页面异常:{}", e.getMessage(), e);
                    }
                }
            }
        }
    }

    private static void pyM3U8(String IPStr, Set<String> hrefs, String domain, Element el, String href) {
        Pattern pattern = compile("(http).*(.m3u8)");
        Matcher ma = pattern.matcher(href);
        while (ma.find()) {
            //判断连接地址是否加密
            String s = JavaScriptUtil.getUrl(ma.group());
            if (!hrefs.contains(s)) {
                hrefs.add(s);
                String title = el.parent().select("title").html();
                try {
                    DownM3U8FileUtil.downM3U8File(s, TEMP_FILE_DIR + "/" + IPStr + "/" + domain + "/" + title, title);
                } catch (Exception e) {
                    log.error("连接无效: {}", s, e);
                }
            } else {
                log.info("已经爬取过此地址：{}", s);
            }
        }
    }

    private static void pySources(String IPStr, Set<String> hrefs, String domain, String attr, Element element, String href, String s, String resPathDir) throws IOException {
        if (!hrefs.contains(s)) {
            hrefs.add(s);
            log.info("路径：{}；数量：{}。", s, hrefs.size());
            Connection.Response resultImageResponse = Jsoup.connect(s).ignoreContentType(true).timeout(10000).execute();
            boolean b = doSaveImgFile(IPStr, resultImageResponse.bodyAsBytes(), domain + resPathDir, getResName(href));
        }
        element.attr(attr, s);
    }

    public static String getResName(String url) {
        url = filterUrl(url);
        String resName = url.substring(url.lastIndexOf("/"));
        if (("/".equals(resName) || StringUtils.isEmpty(resName))) {
            resName = "index.html";
        }
        if (!resName.contains(".")) {
            resName = resName + ".html";
        }
        return resName;
    }

    public static String getResPathDir(String url) {
        url = filterUrl(url);
        return url.substring(0, url.lastIndexOf("/")).replaceAll("\\.", "");
    }

    public static String getResPathNoParam(String url) {
        url = filterUrl(url);
        return url;
    }

    public static String filterUrl(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.contains("#")) {
            url = url.substring(0, url.indexOf("#"));
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.lastIndexOf("/"));
        }
        return url;
    }

    public static String getAlertMsg(String msg) {
        return "<script src=\"https://unpkg.com/sweetalert/dist/sweetalert.min.js\"></script>" +
                "<link rel=\"icon\" type=\"image/x-icon\" href=\"../static/favicon.ico\">" +
                "<script>" +
                "window.onload = function (){" +
                "swal(\"" + msg + "\")" +
                ".then((value) => {" +
                "  window.opener=null;" +
                "  window.open('','_self');" +
                "  window.close();" +
                "});" +
                "}</script>";
    }

}
