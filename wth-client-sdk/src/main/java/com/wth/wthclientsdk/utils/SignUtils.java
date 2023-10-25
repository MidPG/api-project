package com.wth.wthclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 *  签名工具
 */
public class SignUtils {

    public static String genSign(String body, String secretKey){
        // 加密逻辑
        Digester md5 = new Digester(DigestAlgorithm.SHA256);

        String content = body + "." + secretKey;

        return md5.digestHex(content);

    }

}
