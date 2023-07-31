package com.shixun7zu.uilit;

import com.shixun7zu.entity.Account;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtToken {

    private static final long time = 1000*60*60*2;

    private static final String signature = "test";

    public static String creatToken(String username){
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                .setHeaderParam("type","JWT")
                .setHeaderParam("role","test")
                .claim("username",username)
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
    }

    /**
     * 解析jwt，解析时若过期会抛出ExpiredJwtException异常
     * @param  token token值
     * @return jwt对象
     */
    public static Claims parseJwt(String token){
        //解析jwt
        JwtParser parser = Jwts.parser();
        //获取解析后的对象
        return Jwts.parser()
                //设置签名秘钥，和生成的签名的秘钥一模一样
                .setSigningKey(signature)
                //设置需要解析的jwt
                .parseClaimsJws(token)
                .getBody();
    }

}
