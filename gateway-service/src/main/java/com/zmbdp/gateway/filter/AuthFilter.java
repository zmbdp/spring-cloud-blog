package com.zmbdp.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zmbdp.common.pojo.Result;
import com.zmbdp.common.utils.JWTUtils;
import com.zmbdp.gateway.properties.AuthWhiteName;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthWhiteName authWhiteName;

    //    private List<String> whiteList = List.of("/user/login", "/user/register");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 配置白名单, 如果在白名单中, 则不进行验证
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        // 如果当前请求在白名单中, 则不进行验证
        // 就是如果说 whiteList 包含了 path, 就继续进行下一步
        if (match(authWhiteName.getUrl(), path)) {
            return chain.filter(exchange);
        }

        // 从 header 中获取 token
        String userToken = request.getHeaders().getFirst("user_token");
        log.info("从 header 中获取 token: {}", userToken);
        if (!StringUtils.hasLength(userToken)) {
            // 验证码不存在, 拦截
            log.info("token 不存在, 拦截");
            return unauthorizedResponse(exchange, "请先登录, 才能看小博哦~");
        }

        Claims claims = JWTUtils.parseToken(userToken);
        if (claims == null) {
            // 证明 token 不合法
            log.info("令牌验证失败, 拦截");
            return unauthorizedResponse(exchange, "登录时间太久, 小博想不起来你是谁了QAQ");
        }
        log.info("令牌验证通过, 放行");
        return chain.filter(exchange);

    }

    @SneakyThrows // 忽略异常
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String errMsg) {
        log.error("[用户认证失败], url: {}", exchange.getRequest().getPath());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Result result = Result.fail(errMsg);
        DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(result));
        return response.writeWith(Mono.just(buffer));
    }

    private Boolean match(List<String> urls, String path) {

        if (urls == null || urls.isEmpty()) {
            return false;
        }

        return urls.contains(path);
    }


    @Override
    public int getOrder() {
        return -200;
    }

    /**
     * 判断验证码是否正确
     *
     * @param inputCaptcha 用户输入的验证码
     * @return 验证是否通过
     */
//    private Boolean check(String inputCaptcha) {
//        // 从redis里面获取验证码
//    }
}
