package com.wth.wthclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wth.wthclientsdk.model.User;
import com.wth.wthclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  调用第三方接口的客户端
 */
public class TApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private final String accessKey;

    private final String secretKey;

    public TApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        return result;
    }

    public String getNameByPost(String name) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        return result;
    }

    private Map<String, String> getHeadMap(String body){
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("accessKey", accessKey);
        // 密码不能直接传递
        hashMap.put("nonce", RandomUtil.randomNumbers(4));

        hashMap.put("body", body);

        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));

        hashMap.put("sign", SignUtils.genSign(body, secretKey));

        return hashMap;

    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        Map<String, String> headMap = getHeadMap(json);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(headMap)
                .body(json)// 将JSON字符串设置为请求体
                .execute();// 执行请求

        String result = httpResponse.body();
        return result;
    }




}
