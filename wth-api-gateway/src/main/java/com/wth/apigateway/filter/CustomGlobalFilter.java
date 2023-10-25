package com.wth.apigateway.filter;

import cn.hutool.core.util.ObjectUtil;
import com.wth.common.model.entity.InterfaceInfo;
import com.wth.common.model.entity.User;
import com.wth.common.service.InnerInterfaceInfoService;
import com.wth.common.service.InnerUserInterfaceInfoService;
import com.wth.common.service.InnerUserService;
import com.wth.wthclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *  全局过滤，接受请求进行处理
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    InnerUserService innerUserService;

    public static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    public static final long FIVE_MINUTES = 60 * 5L;

    // 用户前端调用的地址来源 ip + 端口号
    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
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
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return handleNoAuth(response);
        }
        // 3. 鉴权（ak,sk）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        boolean allNotEmpty = ObjectUtil.isAllNotEmpty(nonce, body, timestamp, sign, path, method);
        if (!allNotEmpty) {
            return handleInvokeError(response);
        }
        // 数据库中通过 accessKey 查询 secretKey
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        }catch (Exception e) {
            log.error("getInvoker error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 进行 md5加密算法 跟客户端进行同样的加密操作， 得到的数据一直就成功
        String secretKey = invokeUser.getSecretKey();

        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleInvokeError(response);
        }
        //  4. 查找请求的接口是否存在  需要去数据库中查，但是把查询业务写到gateway中不好，会重复引入依赖，也不利于管理。
        //     有两种解决方案：(1)使用Http发送请求，调用方法。(2)使用RPC实现远程调用 本次我们使用 Dubbo来进行调用
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(path, method);
        }catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleInvokeError(response);
        }
        // 5. 请求转发，调用模拟接口
        // 因为 gateway是异步处理请求，所以需要装饰者模式进行装饰代码
//        Mono<Void> filter = chain.filter(exchange);
        // 6. 调用成功，请求次数 +1
        // 7. 调用失败，返回一个错误状态码
        // 8. 打印日志信息
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }


    /**
     *  不可访问的返回方法
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }
}