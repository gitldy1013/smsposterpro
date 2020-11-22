package com.smsposterpro.api.py;

import com.smsposterpro.api.BaseController;
import com.smsposterpro.utils.CusAccessObjectUtil;
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
import java.util.regex.Pattern;

import static com.smsposterpro.utils.HtmlUtils.createFileWithMultilevelDirectory;
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
            htmlFile = "文件还未上传！首次调用需要上传文件";
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

    @PostMapping("/webpy")
    @ResponseBody
    public String webpy(@RequestParam(name = "webpy", required = true) String param, HttpServletRequest request) {
        String regex = "(https?|HTTP)://[-\\w+&@#/%=~|?!:,.;]+[-\\w+&@#/%=~|]";
        Pattern pattern = Pattern.compile(regex);
        if (StringUtils.isEmpty(param) || !pattern.matcher(param).matches()) {
            return "请输入有效爬取链接地址";
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
            FileWriter fw = null;
            try {
                String content = EntityUtils.toString(httpEntity, "UTF-8");
                String[] directories = {TEMP_FILE_DIR, CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "")};
                String rootName = new File(".").getAbsolutePath();
                File tempFile = createFileWithMultilevelDirectory(directories, TEMP_FILE_NAME, rootName);
                fw = new FileWriter(tempFile);
                fw.write(content);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
                log.info("爬取网页信息异常", e);
                return "爬取网页信息异常";
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
        } else {
            log.info("在线爬取网页失败,可能此网址已经设置防爬策略。返回响应码未：：{}", response.getStatusLine().getStatusCode());
            return "在线爬取网页失败,可能此网址已经设置防爬策略。";
        }
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
        log.info(htmlFile);
        return htmlFile;
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "./" + TEMP_FILE_DIR + "/" + CusAccessObjectUtil.getIpAddress(request).replaceAll("\\.", "").replaceAll(":", "") + "/" + TEMP_FILE_NAME;
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(fileName));
            //获取文件后缀（.txt）
            String extendFileName = fileName.substring(fileName.lastIndexOf('.'));
            //动态设置响应类型，根据前台传递文件类型设置响应类型
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            //设置响应头,attachment表示以附件的形式下载，inline表示在线打开
            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            //获取输出流对象（用于写文件）
            ServletOutputStream os = response.getOutputStream();
            //下载文件,使用spring框架中的FileCopyUtils工具
            FileCopyUtils.copy(fis, os);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("获取文件异常", e);
        }


    }
}
