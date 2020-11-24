package com.smsposterpro.api.py;

import com.smsposterpro.api.BaseController;
import com.smsposterpro.exception.AesException;
import com.smsposterpro.utils.CusAccessObjectUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static com.smsposterpro.utils.HtmlUtils.createFileWithMultilevelDirectory;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_EXP_FILE_NAME;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_DIR;
import static com.smsposterpro.utils.ResourcesFileUtils.TEMP_FILE_NAME;

/**
 * 短信转发Controller
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@Controller
@Api(tags = "简单爬虫接口")
@Slf4j
public class PyController extends BaseController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    private String getRes(String param, HttpServletRequest request, String doAct) {
        String htmlFile = null;
        param = StringUtils.isEmpty(param) ? null : param;
        File file = null;
        try {
            file = new File("./" + TEMP_FILE_DIR + "/" + CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "") + "/" + TEMP_FILE_NAME);
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
        if (StringUtils.isEmpty(htmlFile)) {
            htmlFile = "<h2>检查一下检索内容吧~没有找到符合的过滤数据呢!</h2>";
        }
        try {
            FileCopyUtils.copy(htmlFile, new FileWriter(new File("./" + TEMP_FILE_DIR + "/" + CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "") + "/" + TEMP_EXP_FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件保存出错.html");
        }
        return htmlFile;
    }

    @PostMapping("/regcus")
    @ResponseBody
    public String regcus(String regcus, HttpServletRequest request) {
        String doAct = "regcus";
        return getRes(regcus, request, doAct);
    }

    @PostMapping("/filter")
    @ResponseBody
    public String filter(String filter, HttpServletRequest request) {
        String doAct = "filter";
        return getRes(filter, request, doAct);
    }

    @PostMapping("/webpyold")
    @ResponseBody
    public String webpyold(@RequestParam(name = "webpy", required = true) String param, HttpServletRequest request) throws IOException {
        String regex = "(https?|HTTP)://[-\\w+&@#/%=~|?!:,.;]+[-\\w+&@#/%=~|]";
        Pattern pattern = Pattern.compile(regex);
        if (StringUtils.isEmpty(param) || !pattern.matcher(param).matches()) {
            return "<h2>请输入有效爬取链接地址</h2>";
        }
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
            if (doSaveTempFile(request, EntityUtils.toString(httpEntity, "UTF-8"))) return "<h2>爬取网页信息异常</h2>";
        } else {
            log.info("在线爬取网页失败,可能此网址已经设置防爬策略。返回响应码未：：{}", response.getStatusLine().getStatusCode());
            return "<h2>在线爬取网页失败,可能此网址已经设置防爬策略。</h2>";
        }
        return getRes(null, request, "web");
    }

    private boolean doSaveTempFile(HttpServletRequest request, String content) {
        FileWriter fw = null;
        try {
            String[] directories = {TEMP_FILE_DIR, CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "")};
            String rootName = new File(".").getAbsolutePath();
            File tempFile = createFileWithMultilevelDirectory(directories, TEMP_FILE_NAME, rootName);
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

    @PostMapping("/webpy")
    @ResponseBody
    public String webpy(@RequestParam(name = "webpy", required = true) String param, HttpServletRequest request) throws IOException {
        String regex = "(https?|HTTP)://[-\\w+&@#/%=~|?!:,.;]+[-\\w+&@#/%=~|]";
        Pattern pattern = Pattern.compile(regex);
        if (StringUtils.isEmpty(param) || !pattern.matcher(param).matches()) {
            return "<h2>请输入有效爬取链接地址</h2>";
        }
        Document document = Jsoup.connect(param).get();
        doSaveTempFile(request, document.toString());
        return getRes(null, request, "web");
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
        try {
            fis = new FileInputStream(new File(fileName));
            //获取文件后缀（.txt）
            String extendFileName = fileName.substring(fileName.lastIndexOf('.'));
            //动态设置响应类型，根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));
            //获取输出流对象（用于写文件）
            ServletOutputStream os = response.getOutputStream();
            //下载文件,使用spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fis, os);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件异常", e);
        }
    }

    @RequestMapping("/downloadZip")
    public void downloadZip(HttpServletRequest request, HttpServletResponse response) {
        String IpStr = CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "");
        String filePath = "./" + TEMP_FILE_DIR + "/" + IpStr;
        FileInputStream fis;
        //获取输出流对象（用于写文件）
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            FileUtils.fileToZip(filePath, filePath, IpStr);
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
                FileCopyUtils.copy(e.getMessage().getBytes(StandardCharsets.UTF_8), os);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.info("信息数据写出异常", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件异常", e);
            try {
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                FileCopyUtils.copy(e.getMessage().getBytes(StandardCharsets.UTF_8), os);
            } catch (IOException ex) {
                ex.printStackTrace();
                log.info("信息数据写出异常", e);
            }
        }
    }

}
