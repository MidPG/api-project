package com.wth.apigateway.filter;
import com.wth.wthclientsdk.utils.SignUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.SslInfo;

import java.util.*;

import org.springframework.http.server.RequestPath;
import java.net.InetSocketAddress;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *  全局过滤，接受请求进行处理
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    public static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    public static final long FIVE_MINUTES = 60 * 5L;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /**
         *
         *  5. 请求转发，调用模拟接口  相应日志
         *
         *  7. 调用失败，返回错误状态码
         */
        // 1. 用户发送请求  请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径" + request.getPath().value());
        log.info("请求方法" + request.getMethod());
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求来源地址：" + request.getRemoteAddress());
        String localAddress = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        ServerHttpResponse response = exchange.getResponse();
        // 2. 检测白名单
        if (!IP_WHITE_LIST.contains(localAddress)) {
            return handleNoAuth(response);
        }
        // 3. 鉴权（ak,sk）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        if (!"admin".equals(accessKey) || Objects.requireNonNull(nonce).length() > 5) {
            return handleNoAuth(response);
        }
        long currentTime = System.currentTimeMillis() / 1000;
        // todo 数据库中通过 accessKey 查询 secretKey
        // 进行 md5加密算法 跟客户端进行同样的加密操作， 得到的数据一直就成功
        String sign2 = SignUtils.genSign(body, "abcdef");
        if (!sign2.equals(sign)) {
            return handleNoAuth(response);
        }
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        //  4. 查找请求的接口是否存在
//             去数据库中查，但是把查询业务写到gateway中不好，会重复引入依赖，也不利于管理。
//          有两种解决方案：1. 使用Http发送请求，调用方法。 2. 使用RPC实现远程调用 本次我们使用Dubbo来进行调用

        // 6. 调用成功，请求次数 +1
        return chain.filter(exchange);

    }

    /**
     *  不可访问的返回方法
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}