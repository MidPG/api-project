package com.wth.wthinterface.controller;

import com.wth.wthclientsdk.model.User;
import com.wth.wthclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *  名称api
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name) {
        return "GET方法获得你的名字是 " + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "POST方法获得你的名字是 " + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String body = request.getHeader("body");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        if (!"admin".equals(accessKey) || nonce.length() > 5) {
            throw new RuntimeException("无权限");
        }
        // todo 数据库中通过 accessKey 查询 secretKey
        // 进行 md5加密算法 跟客户端进行同样的加密操作， 得到的数据一直就成功
        String sign2 = SignUtils.genSign(body, "abcdef");
        if (!sign2.equals(sign)) {
            throw new RuntimeException("无权限");
        }
        return "POST方法获得你的名字是 " + user.getUsername();
    }

}
