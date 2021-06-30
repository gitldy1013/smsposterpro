package com.smsposterpro.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Auther: dyliu
 * @Date: 2020/11/28 01:50
 * @Description: m3u8视频下载工具类
 */
@Slf4j
public class DownM3U8FileUtil {

    public static final String PREFIX = "完成-";

    public static String downM3U8File(String indexPath, String rootPath, String fileName) {
        File file = new File(rootPath + "/" + fileName + ".mp4");
        File finalFile = new File(rootPath + "/" + PREFIX + fileName + ".mp4");
        if (finalFile.exists()) {
            return fileName + ".mp4 已经下载过。";
        }
        if (file.exists()) {
            return fileName + ".mp4 正在下载中。";
        }
        log.info("文件 " + fileName + ".mp4 开始下载");
        URL url = null;
        try {
            url = new URL(indexPath);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error("视频地址错误！");
        }
        assert url != null;
        //String orgin = url.getProtocol() + "://" + url.getHost() + ((url.getPort() > 0) ? ":" + url.getPort() : "");
        //下载索引文件
        String indexStr = getIndexFile(indexPath);
        if (indexStr == null) {
            return "检索文件下载出错";
        }
        //解析索引文件
        List<String> videoUrlList = analysisIndex(indexStr);
        //生成文件下载目录
        File fileDir = new File(rootPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        HashMap<Integer, String> keyFileMap = new HashMap<>();
        new downLoadNode(videoUrlList, 0, videoUrlList.size() - 1, keyFileMap, indexPath.substring(0, indexPath.lastIndexOf("/") + 1), rootPath).start();
        //等待下载并合成文件
        while (keyFileMap.size() < videoUrlList.size()) {
            log.info("当前下载数量" + keyFileMap.size());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //删除视频片段
        File[] files = fileDir.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.isDirectory()) {
                FileUtils.deleteDir(f, ".mp4", ".jpg", ".jpeg", "png");
            }
        }
        return fileName + ".mp4 已经下载完成。";
    }

    /**
     * 合成视频 删除片段
     */
    private static void composeFile(FileOutputStream finalOutputStream, HashMap<Integer, String> keyFileMap) {
        FileInputStream fis = null;
        File file = null;
        try {
            byte[] bytes = new byte[1024];
            int length;
            for (int i = 0; i < keyFileMap.size(); i++) {
                String nodePath = keyFileMap.get(i);
                if (nodePath == null) {
                    continue;
                }
                file = new File(nodePath);
                if (!file.exists()) {
                    continue;
                }
                fis = new FileInputStream(file);
                while ((length = fis.read(bytes)) != -1) {
                    finalOutputStream.write(bytes, 0, length);
                    finalOutputStream.flush();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                    file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static List<String> analysisIndex(String content) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = compile(".*ts");
        Matcher ma = pattern.matcher(content);
        while (ma.find()) {
            String s = ma.group();
            list.add(s);
            log.info(s);
        }
        return list;
    }

    private static String getIndexFile(String urlpath) {
        try {
            URL url = new URL(urlpath);
            String orgin = url.getProtocol() + "://" + url.getHost() + ((url.getPort() > 0) ? ":" + url.getPort() : "");
            boolean flag = true;
            String flagStr = "";
            //下在资源
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(300000);
            httpURLConnection.setReadTimeout(300000);
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
                if (line.matches(".*m3u8")) {
                    flag = false;
                    flagStr = line;
                    break;
                }
            }
            in.close();
            log.info(content.toString());
            if (!flag) {
                return getIndexFile(orgin + flagStr);
            } else {
                return content.toString();
            }
        } catch (Exception e) {
            log.info("检索文件下载出错：{}", urlpath, e);
            return null;
        }
    }

    static class downLoadNode extends Thread {
        HashMap<Integer, String> keyFileMap;
        private List<String> list;
        private int start;
        private int end;
        private String preUrlPath;
        private String fileRootPath;

        downLoadNode(List<String> list, int start, int end, HashMap<Integer, String> keyFileMap, String preUrlPath, String fileRootPath) {
            this.list = list;
            this.end = end;
            this.start = start;
            this.keyFileMap = keyFileMap;
            this.preUrlPath = preUrlPath;
            this.fileRootPath = fileRootPath;
        }

        @Override
        public void run() {
            String[] split = fileRootPath.split("/");
            FileOutputStream finalOutputStream = null;
            File finalFile = new File(fileRootPath + "/" + split[split.length - 1] + ".mp4");
            String urlStr = "";
            try {
                finalOutputStream = new FileOutputStream(finalFile);
                for (int i = start; i <= end; i++) {
                    String urlpath = list.get(i);
                    urlStr = ResourcesFileUtils.findMaxSubString(preUrlPath, urlpath);
                    URL url = new URL(urlStr);
                    //下载资源
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setConnectTimeout(300000);
                    httpURLConnection.setReadTimeout(300000);
                    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
                    DataInputStream dataInputStream = new DataInputStream(httpURLConnection.getInputStream());
                    String fileOutPath = fileRootPath + "/" + ((!urlpath.contains("/")) ? urlpath : urlpath.substring(urlpath.lastIndexOf("/")+1));
                    String rootPath = new File(".").getAbsolutePath();
                    String fileName = fileOutPath.substring(fileOutPath.lastIndexOf("/"));
                    HtmlUtils.createFileWithMultilevelDirectory(split, fileName, rootPath);
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(fileOutPath));
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = dataInputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, length);
                        fileOutputStream.flush();
                    }
                    dataInputStream.close();
                    fileOutputStream.close();
                    keyFileMap.put(i, fileOutPath);
                    composeFile(finalOutputStream, keyFileMap);
                }
                log.info("文件" + split[split.length - 1] + ".mp4 已经下载并合成已完成");
            } catch (Exception e) {
                log.info("当前下载异常:{}", urlStr, e);
                e.printStackTrace();
            } finally {
                if (finalOutputStream != null) {
                    try {
                        finalOutputStream.close();
                        finalFile.renameTo(new File(finalFile.getParent() + "/" + PREFIX + finalFile.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
