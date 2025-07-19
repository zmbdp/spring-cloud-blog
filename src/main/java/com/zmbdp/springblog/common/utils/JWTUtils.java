package com.zmbdp.springblog.common.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtils {
    // 令牌有效期
    private static final long EXPIRATION = 1000 * 60 * 30;
    // 令牌密钥
    private static final String SECRET_STRING = "9dl2i3/fBSn9O64/LsHZQYzFNQ+Zbu4lg6igM3rvB5Q=";
    // 令牌前缀
    private static final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_STRING));

    /**
     * 生成 token
     * @param claim
     * @return
     */
    public static String genJwt(Map<String, Object> claim) {
        String resultJWT = Jwts.builder()
                .setClaims(claim)// 设置载荷信息
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))// 设置过期时间
                .signWith(key)// 设置签名
                .compact();// 然后生成 token
        return resultJWT;
    }

    /**
     * 校验 token
     * @param jwt 待校验的 token
     * @return 校验结果
     */
    public static Claims parseToken(String jwt){
        if (!StringUtils.hasLength(jwt)){
            return null;
        }
        JwtParser jwtParserBuilder = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = null;
        try {
            claims = jwtParserBuilder.parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            log.error("解析令牌错误, jwt: {}",jwt);
            return null;
        }
        return claims;
    }

    public static Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Map<String, Object> userInfo = new HashMap<>(claims);
            return (Integer) userInfo.get("id");
        }
        return null;
    }
}
