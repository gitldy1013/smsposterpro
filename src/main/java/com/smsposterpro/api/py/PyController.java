package com.smsposterpro.api.py;

import com.smsposterpro.api.BaseController;
import com.smsposterpro.service.sms.SmsMsgService;
import com.smsposterpro.utils.HtmlUtils;
import com.smsposterpro.utils.ResourcesFileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

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

    @Autowired
    private SmsMsgService userService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @ResponseBody
    @RequestMapping("/upload")
    public String upload(MultipartFile file, String flag, HttpServletRequest request) throws IOException {
        //获取文件的原始名
        String filename = file.getOriginalFilename();
        //根据相对路径获取绝对路径
        assert filename != null;
        InputStream inputStream = file.getInputStream();
        File resourcesFile = ResourcesFileUtils.getResourcesFile(inputStream);
        String htmlFile = HtmlUtils.readHtmlFile(resourcesFile, flag);
        log.info(htmlFile);
        return htmlFile;
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        //获取文件的绝对路径
        //获取输入流对象（用于读文件）
        String fileName = TEMP_FILE_NAME;
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
