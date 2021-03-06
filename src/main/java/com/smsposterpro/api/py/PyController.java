package com.smsposterpro.api.py;

import com.smsposterpro.api.BaseController;
import com.smsposterpro.exception.AesException;
import com.smsposterpro.service.MailService;
import com.smsposterpro.utils.CusAccessObjectUtil;
import com.smsposterpro.utils.DownM3U8FileUtil;
import com.smsposterpro.utils.FileUtils;
import com.smsposterpro.utils.HtmlUtils;
import com.smsposterpro.utils.ResourcesFileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.smsposterpro.utils.CommonUtils.getTime;
import static com.smsposterpro.utils.HtmlUtils.createFileWithMultilevelDirectory;
import static com.smsposterpro.utils.HtmlUtils.doSaveTempFile;
import static com.smsposterpro.utils.HtmlUtils.getAlertMsg;
import static com.smsposterpro.utils.HtmlUtils.getRes;
import static com.smsposterpro.utils.HtmlUtils.getResPathNoParam;
import static com.smsposterpro.utils.HtmlUtils.regUrl;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_EXP_FILE_NAME;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_DIR;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_NAME;

/**
 * 简单爬虫接口Controller
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Controller
@Api(tags = "简单爬虫接口")
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PyController extends BaseController {
    //暂定每页视频数
    public static final int MAXPY = 20;

    public static ExecutorService executorFix = null;

    volatile LinkedHashSet<String> hrefs = new LinkedHashSet<>();

    @Autowired
    private MailService mailService;

    /**
     * 信任任何站点
     */
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/regcus")
    @ResponseBody
    public String regcus(String regcus, HttpServletRequest request) {
        String doAct = "regcus";
        String IPStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        return getRes(regcus, IPStr, doAct);
    }

    @PostMapping("/filter")
    @ResponseBody
    public String filter(String filter, HttpServletRequest request) {
        String doAct = "filter";
        String IPStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        return getRes(filter, IPStr, doAct);
    }

    @PostMapping("/webpyold")
    @ResponseBody
    public String webpyold(@RequestParam(name = "webpy", required = true) String param, HttpServletRequest request) throws IOException {
        if (regUrl(param)) return "<h2>请输入有效爬取链接地址</h2>";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(param);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity httpEntity = response.getEntity();
            String IPStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
            if (doSaveTempFile(IPStr, EntityUtils.toString(httpEntity, "UTF-8"))) return "<h2>爬取网页信息异常</h2>";
        } else {
            log.info("在线爬取网页失败,可能此网址已经设置防爬策略。返回响应码为：{}", response.getStatusLine().getStatusCode());
            return "<h2>在线爬取网页失败,可能此网址已经设置防爬策略。</h2>";
        }
        String IPStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        return getRes(null, IPStr, "web");
    }

    @PostMapping("/webpy")
    @ResponseBody
    public String webpy(@RequestParam(name = "webpy", required = true) String param, HttpServletRequest request) throws IOException {
        if (regUrl(param)) return "<h2>请输入有效爬取链接地址</h2>";
        Document document = Jsoup.connect(param).get();
        String IPStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        doSaveTempFile(IPStr, document.toString());
        return getRes(null, IPStr, "web");
    }

    @PostMapping("/webpyAll")
    @ResponseBody
    public String webpyAll(@RequestParam(name = "webpyAll", required = true) String param, HttpServletRequest request) {
        if (!regUrl(param)) {
            String IpStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
            long start = System.currentTimeMillis();
            //开始压缩文件
            String filePath = "./" + TEMP_FILE_DIR + "/" + IpStr;
            if (!StringUtils.isEmpty(param)) {
                URL url;
                try {
                    url = new URL(param);
                    String host = url.getHost().replaceAll("\\.", "");
                    filePath += "/" + host;
                    FileUtils.count = 0;
                    int count = FileUtils.deleteDirWithCount(FileUtils.DELETEDIRSTR, "mp4", "jpg", "jpeg", "png");
                    log.info("总文件数为：" + count);
                    //开始爬取文件
                    String resPathNoParamPath = getResPathNoParam(url.getPath());
                    if (resPathNoParamPath.length() == 0) {
                        resPathNoParamPath = "/";
                    }
                    String finalFilePath = filePath;
                    String finalResPathNoParamPath = resPathNoParamPath;
                    new Thread(() -> {
                        try {
                            if (executorFix != null && !executorFix.isShutdown()) {
                                executorFix.shutdownNow();
                            }
                            executorFix = Executors.newFixedThreadPool(MAXPY);
                            HtmlUtils.getArticleURLs(IpStr, param, hrefs, finalResPathNoParamPath.substring(0, finalResPathNoParamPath.lastIndexOf("/")));
                            List<Future<String>> futures = PyController.executorFix.invokeAll(DownM3U8FileUtil.downLoadNodes);
                            for (Future<String> future : futures) {
                                log.info("爬取结果：" + future.get());
                            }
                            DownM3U8FileUtil.downLoadNodes.clear();
                            executorFix.shutdown();
                            executorFix.awaitTermination(1, TimeUnit.DAYS);
                            if (executorFix.isTerminated()) {
                                try {
                                    System.out.println("发送通知");
                                    File zipFile = new File(finalFilePath + "/" + IpStr + ".zip");
                                    if (!zipFile.exists()) {
                                        FileUtils.fileToZip(finalFilePath, finalFilePath, IpStr);
                                        long end = System.currentTimeMillis();
                                        //发邮件TODO
                                        log.info("爬取任务：" + param + "完成，开始发送邮件,推送微信消息。");
                                        String context = IpStr + "-相关爬取任务已经完成</br>" +
                                                "本次总共爬取文件数量为：" + hrefs.size() + "个；总耗时" + getTime(end - start) + ";</br>" +
                                                "请点击连接:<a href='https://sms.liudongyang.top/downloadZip?subPath=" + param + "'>点击下载</a>";
                                        //mailService.sendMimeMessge("1126176532@qq.com", "爬取任务完成通知", context);
                                        //String forObject = restTemplate.getForObject("http://sc.ftqq.com/SCU125307T7c9f252f885c51edad0e59ea4a37a64f5faa5441b53e5.send?text=相关爬取任务已经完成&desp=" + context, String.class);
                                        //log.info("微信推送成功：{}", forObject);
                                    }
                                } catch (AesException e) {
                                    log.info("压缩文件失败！", e);
                                    //mailService.sendMimeMessge("1126176532@qq.com", "爬取任务完成通知", IpStr + "-相关爬取任务压缩文件发生异常" + e.getMessage());
                                    //String forObject = restTemplate.getForObject("http://sc.ftqq.com/SCU125307T7c9f252f885c51edad0e59ea4a37a64f5faa5441b53e5.send?text=相关爬取任务压缩文件发生异常&desp=失败原因：" + e.getMessage(), String.class);
                                    //log.info("微信推送成功：{}", forObject);
                                }
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } catch (MalformedURLException e) {
                    log.error("url参数异常！", e);
                }
            }
            System.out.println("任务启动完成");
            return "<h2>后台任务已经启动</h2>";
        } else {
            return "<h2>请输入有效爬取链接地址</h2>";
        }
    }

    @ResponseBody
    @RequestMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest request) throws IOException {
        InputStream inputStream = file.getInputStream();
        String[] directories = {TEMP_FILE_DIR, CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "")};
        String rootName = new File(".").getAbsolutePath();
        File tempFile = createFileWithMultilevelDirectory(directories, TEMP_FILE_NAME, rootName);
        File resourcesFile = ResourcesFileUtils.getResourcesFile(inputStream, tempFile);
        String htmlFile = HtmlUtils.readHtmlFile(resourcesFile, null);
        String fileName = "./" + TEMP_FILE_DIR + "/" + CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "") + "/" + file.getOriginalFilename();
        FileCopyUtils.copy(htmlFile, new FileWriter(new File(fileName)));
        log.info(htmlFile);
        return htmlFile;
    }

    @RequestMapping("/download")
    public void download(String filename, HttpServletRequest request, HttpServletResponse response) {
        filename = (StringUtils.isEmpty(filename)) ? TEMP_EXP_FILE_NAME : filename;
        String fileName = "./" + TEMP_FILE_DIR + "/" + CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "") + "/" + filename;
        FileInputStream fis;
        //获取输出流对象（用于写文件）
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(new File(fileName));
            //获取文件后缀（.txt）
            String extendFileName = fileName.substring(fileName.lastIndexOf('.'));
            //动态设置响应类型，根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));
            //下载文件,使用spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fis, os);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件异常", e);
            try {
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                response.setContentType("text/html; charset=utf-8");
                FileCopyUtils.copy(getAlertMsg(e.getMessage()).getBytes(StandardCharsets.UTF_8), os != null ? os : response.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                log.info("信息数据写出异常", e);
            }
        }
    }

    @RequestMapping("/downloadZip")
    public void downloadZip(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "subPath", required = false) String subPath) {
        String IpStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        String filePath = "./" + TEMP_FILE_DIR + "/" + IpStr;
        FileInputStream fis;
        //获取输出流对象（用于写文件）
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            if (!StringUtils.isEmpty(subPath)) {
                URL url = new URL(subPath);
                String host = url.getHost().replaceAll("\\.", "");
                filePath += "/" + host;
                IpStr = host;
            }
            File zipFile = new File(filePath + "/" + IpStr + ".zip");
            if (!zipFile.exists()) {
                FileUtils.fileToZip(filePath, filePath, IpStr);
            }
            String fileName = IpStr + ".zip";
            fis = new FileInputStream(new File(filePath + "/" + fileName));
            //获取文件后缀（.txt）
            String extendFileName = fileName.substring(fileName.lastIndexOf('.'));
            //动态设置响应类型，根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            //下载文件,使用spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fis, os);
        } catch (AesException e) {
            e.printStackTrace();
            try {
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                response.setContentType("text/html; charset=utf-8");
                FileCopyUtils.copy(getAlertMsg(e.getMessage()).getBytes(StandardCharsets.UTF_8), os);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.info("信息数据写出异常", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件异常", e);
            try {
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                response.setContentType("text/html; charset=utf-8");
                FileCopyUtils.copy(getAlertMsg(e.getMessage()).getBytes(StandardCharsets.UTF_8), os != null ? os : response.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                log.info("信息数据写出异常", e);
            }
        }
    }

}
