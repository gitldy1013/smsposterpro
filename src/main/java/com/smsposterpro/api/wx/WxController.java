package com.smsposterpro.api.wx;


import com.smsposterpro.api.BaseController;
import com.smsposterpro.dto.WeChatMessageBo;
import com.smsposterpro.dto.WeChatMessageVo;
import com.smsposterpro.exception.AesException;
import com.smsposterpro.service.wx.WeChatService;
import com.smsposterpro.utils.SignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 微信服务Controller
 *
 * @author 136****3167
 * @date 2020/10/20 12:20
 */
@RestController
@RequestMapping("/wx")
@Api(tags = "微信服务接口")
@Slf4j
public class WxController extends BaseController {

    @Autowired(required = false)
    private WeChatService weChatService;
    private Unmarshaller unmarshaller;
    private Marshaller marshaller;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation("微信权限验证")
    public String wx(@RequestParam(value = "signature", required = false) String signature, @RequestParam(value = "timestamp", required = false) String timestamp,
                     @RequestParam(value = "nonce", required = false) String nonce, @RequestParam(value = "echostr", required = false) String echostr) throws AesException {
        return SignUtil.checkSignature(signature, timestamp, nonce) ? echostr : "";
    }

    /**
     * Bean初始化中的执行顺序：
     *
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    private void init() {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(WeChatMessageBo.class);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            log.info(e.toString());
        }
        try {
            context = JAXBContext.newInstance(WeChatMessageVo.class);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        } catch (JAXBException e) {
            log.info(e.toString());
        }
    }

    @ApiOperation(value = "接收事件")
    @RequestMapping(value = "auth", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    public void event(HttpServletRequest request, HttpServletResponse response) {
        try {
            WeChatMessageBo weChatMessageBo = (WeChatMessageBo) unmarshaller.unmarshal(request.getInputStream());
            Object res = weChatService.processRequest(weChatMessageBo);
            response.setCharacterEncoding("UTF-8");
            marshaller.marshal(res, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
