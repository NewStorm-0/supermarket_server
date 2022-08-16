package com.newstorm.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.newstorm.exception.BaseException;
import com.newstorm.exception.JwtException;
import com.newstorm.pojo.User;
import kotlin.UByte;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    private static final String SECRET = "!34ADAS";

    /**
     * 获取token
     * @param user 信息
     * @return token
     */
    public static String getToken(User user) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 4);

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("userAccount", user.getAccount())
                .withClaim("userLevel", user.getLevel());

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证token合法性
     */
    public static DecodedJWT verify(String jwt) throws JwtException {
        String token = jwt.substring(JwtUtils.TOKEN_PREFIX.length());
        JWTVerifier build = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return build.verify(token);
    }

    public static Map<String, Object> getInformation(String jwt) {
        DecodedJWT decodedJWT = verify(jwt);
        Claim userAccount = decodedJWT.getClaim("userAccount");
        Claim userName = decodedJWT.getClaim("userLevel");
        Map<String, Object> map = new HashMap<>(2);
        map.put("userAccount", userAccount.asInt());
        map.put("userLevel", userName.asString());
        map.put("overtimeTime", decodedJWT.getExpiresAt());
        return  map;
    }

    public static Integer getUserAccount(String jwt) {
        DecodedJWT decodedJWT = verify(jwt);
        Claim userAccount = decodedJWT.getClaim("userAccount");
        return userAccount.asInt();
    }

    public static Integer getUserLevel(String jwt) {
        DecodedJWT decodedJWT = verify(jwt);
        Claim userLevel = decodedJWT.getClaim("userLevel");
        return userLevel.asInt();
    }
}
