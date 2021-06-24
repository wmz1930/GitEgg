package com.gitegg.oauth.util;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import net.minidev.json.JSONObject;

import java.text.ParseException;

/**
 * @author GitEgg
 * @date 2021-03-10 18:11:25
 **/
public class JwtUtils {

    public static JSONObject decodeJwt(String jwt)
    {
        JWSObject jwsObject;
        JSONObject jsonObject = null;
        try {
            jwsObject = JWSObject.parse(jwt);
            Payload payload = jwsObject.getPayload();
            jsonObject = payload.toJSONObject();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
