package com.heyu.travel.web;

import com.heyu.travel.req.BaseRequest;
import com.heyu.travel.res.GatewayResp;
import com.heyu.travel.service.GatewayService;
import com.heyu.travel.utils.EmptyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Description 后台网关
 */
@Controller
//窄化路径
@RequestMapping("/api-platform")
@Log4j2
public class ApiPlatformController {

    @Autowired
    GatewayService gatewayService;

    @RequestMapping("/{service}/{method}")
    @ResponseBody
    //解决跨域问题
    @CrossOrigin
    public GatewayResp apiLoan(@PathVariable(required = false) String service,
                               @PathVariable(required = false) String method,
                               MultipartFile file,
                               @RequestBody BaseRequest<?> baseRequest){
        GatewayResp resp = null;
        if(EmptyUtil.isNullOrEmpty(service) || EmptyUtil.isNullOrEmpty(method)) {
            log.warn("api-loan请求不合法！---> 调用者[{}]--->调用方法[{}]",service,method);
            return GatewayResp.builder().data(null).code("1008").msg("请求参数不合法").build();
        }else {
            log.info("api-loan请求：调用类[{}]--->方法[{}]--->请求参数[{}]",
                    service, method, baseRequest.toString());
            resp = gatewayService.method(method,baseRequest,file);
        }
        return resp;
    }

    @RequestMapping(value = "/checkCaptchaCode.do", method = RequestMethod.GET)
    @CrossOrigin
    public void validateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //服务器通知浏览器不要缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");

        //在内存中创建一个长80，宽30的图片，默认黑色背景
        //参数一：长
        //参数二：宽
        //参数三：颜色
        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //获取画笔
        Graphics g = image.getGraphics();
        //设置画笔颜色为灰色
        g.setColor(Color.GRAY);
        //填充图片
        g.fillRect(0, 0, width, height);

        //产生4个随机验证码，12Ey
        String checkCode = getCheckCode();
        //将验证码放入HttpSession中
        request.getSession().setAttribute("CHECKCODE_SERVER", checkCode);

        //设置画笔颜色为黄色
        g.setColor(Color.YELLOW);
        //设置字体的小大
        g.setFont(new Font("黑体", Font.BOLD, 24));
        //向图片上写入验证码
        g.drawString(checkCode, 15, 25);

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        ImageIO.write(image, "PNG", response.getOutputStream());
    }
    /**
     * 产生4位随机字符串
     */
    private String getCheckCode() {
        String base = "0123456789ABCDEFGabcdefg";
        int size = base.length();
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=1;i<=4;i++){
            //产生0到size-1的随机值
            int index = r.nextInt(size);
            //在base字符串中获取下标为index的字符
            char c = base.charAt(index);
            //将c放入到StringBuffer中去
            sb.append(c);
        }
        return sb.toString();
    }
}
