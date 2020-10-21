package com.smsposterpro.api.sms;

import com.smsposterpro.api.BaseController;
import com.smsposterpro.core.model.ApiResponse;
import com.smsposterpro.core.model.PageWrap;
import com.smsposterpro.dao.user.model.SmsMsg;
import com.smsposterpro.service.sms.SmsMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 示例Controller
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@RestController
@RequestMapping("/task")
@Api(tags = "示例接口")
public class SmsController extends BaseController {

    @Autowired
    private SmsMsgService userService;

    @PostMapping("/post")
    @ApiOperation("推送")
    public String post(@RequestParam("from") String from, @RequestParam("msg") String msg) {
        SmsMsg smsMsg = new SmsMsg();
        smsMsg.setSendPhoneNum(from);
        smsMsg.setContext(msg);
        smsMsg.setSendTime(new Date());
        ApiResponse<SmsMsg> smsMsgApiResponse = create(smsMsg);
        Boolean success = smsMsgApiResponse.getSuccess();
        return success ? "推送成功" : "推送失败";
    }

    /**
     * 创建
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @PostMapping("/put")
    @ApiOperation("创建")
    public ApiResponse<SmsMsg> create(@RequestBody SmsMsg req) {
        return ApiResponse.success(userService.create(req));
    }

    /**
     * 通过id查询
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询")
    public ApiResponse<SmsMsg> finById(@PathVariable Integer id) {
        return ApiResponse.success(userService.findById(id));
    }

    /**
     * 分页查询
     *
     * @author Caesar Liu
     * @date 2020-06-27 14:31
     */
    @PostMapping("/list")
    @ApiOperation("分页查询")
    public ApiResponse findPage(@RequestBody PageWrap<SmsMsg> pageWrap) {
        return ApiResponse.success(userService.findPage(pageWrap));
    }

    @GetMapping("/")
    @ApiOperation("查询")
    public void list(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<SmsMsg> list = userService.findList(new SmsMsg());
        res.setHeader("Content-Type", "text/html; charset=UTF-8");
        PrintWriter writer = res.getWriter();
        writer.println("<table  border='2'>");
        writer.println("<tr>");
        writer.println("<th>序号ID</th>");
        writer.println("<th>电话号码</th>");
        writer.println("<th>信息内容</th>");
        writer.println("<th>发送时间</th>");
        writer.println("</tr>");
        SimpleDateFormat sdf = new SimpleDateFormat();
        for (SmsMsg smsMsg : list) {
            writer.println("<tr>");
            writer.println("<td>" + smsMsg.getId() + "</td>");
            writer.println("<td>" + smsMsg.getSendPhoneNum() + "</td>");
            writer.println("<td>" + smsMsg.getContext() + "</td>");
            writer.println("<td>" + sdf.format(smsMsg.getSendTime()) + "</td>");
            writer.println("</tr>");
        }
        writer.println("</table>");
        writer.close();
    }

    /**
     * 根据ID修改
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @PostMapping("/updateById")
    @ApiOperation("根据ID修改")
    public ApiResponse<SmsMsg> updateById(@RequestBody SmsMsg req) {
        userService.updateById(req);
        return ApiResponse.success(null);
    }

    /**
     * 根据ID删除
     *
     * @author 136****3167
     * @date 2020/10/20 12:20
     */
    @GetMapping("/delete/{id}")
    @ApiOperation("根据ID删除")
    public ApiResponse delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return ApiResponse.success(null);
    }
}