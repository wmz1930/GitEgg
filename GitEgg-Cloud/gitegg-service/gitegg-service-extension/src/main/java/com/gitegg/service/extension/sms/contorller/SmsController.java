package com.gitegg.service.extension.sms.contorller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
* <p>
* 短信配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2021-01-25
*/
@RestController
@RequestMapping("/extension/sms")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "SmsController|短信配置表前端控制器")
@RefreshScope
public class SmsController {


 }
