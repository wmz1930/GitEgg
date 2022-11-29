package com.gitegg.gateway.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * XSS注入过滤工具类
 * @author GitEgg
 */
public class XssInjectionRuleUtils {
    
    private static final Pattern[] PATTERNS = {
            
            // Avoid anything in a <script> type of expression
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            // Avoid anything in a src='...' type of expression
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Remove any lonesome </script> tag
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            // Avoid anything in a <iframe> type of expression
            Pattern.compile("<iframe>(.*?)</iframe>", Pattern.CASE_INSENSITIVE),
            // Remove any lonesome <script ...> tag
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Remove any lonesome <img ...> tag
            Pattern.compile("<img(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid eval(...) expressions
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid expression(...) expressions
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            // Avoid javascript:... expressions
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            // Avoid vbscript:... expressions
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            // Avoid onload= expressions
            Pattern.compile("on(load|error|mouseover|submit|reset|focus|click)(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };
    
    public static String stripXSS(String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        for (Pattern scriptPattern : PATTERNS) {
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }
    
    public static boolean hasStripXSS(String value) {
        if (!StringUtils.isEmpty(value)) {
            for (Pattern scriptPattern : PATTERNS) {
                if (scriptPattern.matcher(value).find() == true)
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * xss注入校验 map
     *
     * @param map
     * @return
     */
    public static boolean mapRequestSqlKeyWordsCheck(MultiValueMap<String, String> map) {
        //对post请求参数值进行sql注入检验
        return map.entrySet().stream().parallel().anyMatch(entry -> {
            //这里需要将参数转换为小写来处理
            String lowerValue = Optional.ofNullable(entry.getValue())
                    .map(Object::toString)
                    .map(String::toLowerCase)
                    .orElse("");
            if (hasStripXSS(lowerValue)) {
                return true;
            }
            return false;
        });
    }
    
    
    /**
     *  xss注入校验 json
     *
     * @param value
     * @return
     */
    public static boolean jsonRequestSqlKeyWordsCheck(String value) {
        if (JSONUtil.isJsonObj(value)) {
            JSONObject json = JSONUtil.parseObj(value);
            Map<String, Object> map = json;
            //对post请求参数值进行sql注入检验
            return map.entrySet().stream().parallel().anyMatch(entry -> {
                //这里需要将参数转换为小写来处理
                String lowerValue = Optional.ofNullable(entry.getValue())
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .orElse("");
                if (hasStripXSS(lowerValue)) {
                    return true;
                }
                return false;
            });
        } else {
            JSONArray json = JSONUtil.parseArray(value);
            List<Object> list = json;
            //对post请求参数值进行sql注入检验
            return list.stream().parallel().anyMatch(obj -> {
                //这里需要将参数转换为小写来处理
                String lowerValue = Optional.ofNullable(obj)
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .orElse("");
                if (hasStripXSS(lowerValue)) {
                    return true;
                }
                return false;
            });
        }
    }
    
}
