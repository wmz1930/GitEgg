package com.gitegg.oauth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.gitegg.oauth.dto.SmsVerificationDTO;
import com.gitegg.oauth.util.CaptchaUtils;
import com.gitegg.oauth.util.JwtUtils;
import com.gitegg.platform.base.annotation.auth.CurrentUser;
import com.gitegg.platform.base.constant.AuthConstant;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.constant.TokenConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.enums.ResultCodeEnum;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.captcha.constant.CaptchaConstant;
import com.gitegg.platform.captcha.domain.ImageCaptcha;
import com.gitegg.platform.oauth2.domain.Oauth2Token;
import com.gitegg.service.extension.client.feign.ISmsFeign;
import com.gitegg.service.system.client.feign.IUserFeign;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * OAuth2????????????
 * @author GitEgg
 */
@Api(tags = "OAuth2????????????")
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/oauth")
public class GitEggOAuthController {

    private final TokenEndpoint tokenEndpoint;

    private final RedisTemplate redisTemplate;

    private final KeyPair keyPair;

    private final CaptchaService captchaService;

    private final IUserFeign userFeign;

    private final ISmsFeign smsFeign;

    @Value("${captcha.type}")
    private String captchaType;

    @ApiOperation("????????????????????????????????????")
    @GetMapping("/captcha/type")
    public Result captchaType() {
        return Result.data(captchaType);
    }

    @ApiOperation("?????????????????????")
    @PostMapping("/captcha")
    public Result captcha(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.get(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("?????????????????????")
    @PostMapping("/captcha/check")
    public Result captchaCheck(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.check(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("?????????????????????")
    @RequestMapping("/captcha/image")
    public Result captchaImage() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String captchaCode = specCaptcha.text().toLowerCase();
        String captchaKey = UUID.randomUUID().toString();
        // ??????redis????????????????????????5??????
        redisTemplate.opsForValue().set(CaptchaConstant.IMAGE_CAPTCHA_KEY + captchaKey, captchaCode, GitEggConstant.Number.FIVE,
            TimeUnit.MINUTES);
        ImageCaptcha imageCaptcha = new ImageCaptcha();
        imageCaptcha.setCaptchaKey(captchaKey);
        imageCaptcha.setCaptchaImage(specCaptcha.toBase64());
        // ???key???base64???????????????
        return Result.data(imageCaptcha);
    }

    @ApiOperation("OAuth2??????token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "????????????", required = true),
            @ApiImplicitParam(name = "client_id", defaultValue = "client", value = "Oauth2?????????ID", required = true),
            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2???????????????", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "??????token"),
            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "???????????????"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "????????????"),
            @ApiImplicitParam(name = "phoneNumber", value = "????????????"),
            @ApiImplicitParam(name = "smsCode", value = "????????????code"),
            @ApiImplicitParam(name = "code", value = "?????????code/???????????????"),
            @ApiImplicitParam(name = "encryptedData", value = "????????????????????????????????????????????????????????????"),
            @ApiImplicitParam(name = "iv", value = "???????????????????????????"),
    })
    @PostMapping("/token")
    public Result postAccessToken( @ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        //??????????????????????????????account???md5????????????
        String username = parameters.get("username");
        String password = parameters.get("password");
        Result<Object> result = userFeign.queryUserByAccount(username);
        if (null != result && result.isSuccess()) {
            GitEggUser gitEggUser = new GitEggUser();
            BeanUtil.copyProperties(result.getData(), gitEggUser, false);
            if (!StringUtils.isEmpty(gitEggUser.getAccount())) {
                username = gitEggUser.getAccount();
                password = AuthConstant.BCRYPT + gitEggUser.getAccount() + password;
                parameters.put("username", username);
                parameters.put("password", password);
            }
        }

        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken)oAuth2AccessToken.getRefreshToken();
        Oauth2Token oauth2Token = Oauth2Token.builder()
                .token(oAuth2AccessToken.getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .exp(oAuth2AccessToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND)
                .refreshToken(refreshToken.getValue())
                .refreshExpiresIn((int) (refreshToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND - System.currentTimeMillis() / GitEggConstant.Number.THOUSAND))
                .refreshExp(refreshToken.getExpiration().getTime() / GitEggConstant.Number.THOUSAND)
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX)
                .build();
        return Result.data(oauth2Token);
    }

    @ApiOperation("???????????????????????????")
    @PostMapping("/sms/captcha/send")
    public Result sendSmsCaptcha(@ApiIgnore @RequestParam Map<String, String> parameters) {
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        Result<Object> sendResult;
        if (checkCaptchaResult)
        {
            sendResult = smsFeign.sendSmsVerificationCode(parameters.get(TokenConstant.SMS_CODE), parameters.get(TokenConstant.PHONE_NUMBER));
        }
        else {
            throw new BusinessException("?????????????????????????????????????????????????????????");
        }
        return sendResult;
    }

    /**
     * ????????????????????????????????????????????????
     * 1?????????????????????????????????????????????????????????????????????token??????????????????????????????token???????????????????????????????????????????????????????????????token???????????????redis??????
     * 2???????????????????????????????????????????????????????????????token??????????????????refresh_token????????????????????????????????????
     * ??????????????????????????????????????????????????????
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {

        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String refreshToken = request.getParameter(AuthConstant.REFRESH_TOKEN);
        long currentTimeSeconds = System.currentTimeMillis() / GitEggConstant.Number.THOUSAND;

        // ???token???refresh_token?????????????????????
        String[] tokenArray = new String[GitEggConstant.Number.TWO];
        tokenArray[GitEggConstant.Number.ZERO] = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        tokenArray[GitEggConstant.Number.ONE] = refreshToken;
        for (int i = GitEggConstant.Number.ZERO; i < tokenArray.length; i++) {
            String realToken = tokenArray[i];
            JSONObject jsonObject = JwtUtils.decodeJwt(realToken);
            String jti = jsonObject.getAsString(TokenConstant.JTI);
            Long exp = Long.parseLong(jsonObject.getAsString(TokenConstant.EXP));
            if (exp - currentTimeSeconds > GitEggConstant.Number.ZERO) {
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST + jti, jti, (exp - currentTimeSeconds), TimeUnit.SECONDS);
            }
        }
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result<GitEggUser> currentUser(@ApiIgnore @CurrentUser GitEggUser user) {
        return Result.data(user);
    }

    @GetMapping("/public_key")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
