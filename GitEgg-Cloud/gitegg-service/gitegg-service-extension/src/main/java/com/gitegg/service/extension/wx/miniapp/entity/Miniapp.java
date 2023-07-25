package com.gitegg.service.extension.wx.miniapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
* <p>
* 微信小程序配置
* </p>
*
* @author GitEgg
* @since 2023-07-16
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_wechat_miniapp")
@ApiModel(value = "Miniapp对象", description = "微信小程序配置")
public class Miniapp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "微信小程序名称")
    @TableField("miniapp_name")
    private String miniappName;

    @ApiModelProperty(value = "微信小程序appid")
    @TableField("appid")
    private String appid;

    @ApiModelProperty(value = "微信小程序secret")
    @TableField("secret")
    private String secret;

    @ApiModelProperty(value = "微信小程序token")
    @TableField("token")
    private String token;

    @ApiModelProperty(value = "微信小程序aesKey")
    @TableField("aes_key")
    private String aesKey;

    @ApiModelProperty(value = "消息格式，XML或者JSON")
    @TableField("msg_data_format")
    private String msgDataFormat;

    @ApiModelProperty(value = "配置类型: Memory(默认), Jedis, RedisTemplate")
    @TableField("storage_type")
    private String storageType;

    @ApiModelProperty(value = "相关redis前缀配置: wa(默认)")
    @TableField("key_prefix")
    private String keyPrefix;

    @ApiModelProperty(value = "Redis服务器地址")
    @TableField("redis_host")
    private String redisHost;

    @ApiModelProperty(value = "Redis服务器端口")
    @TableField("redis_port")
    private Integer redisPort;

    @ApiModelProperty(value = "http客户端类型: HttpClient(默认), OkHttp, JoddHttp")
    @TableField("http_client_type")
    private String httpClientType;

    @ApiModelProperty(value = "http_proxy_host")
    @TableField("http_proxy_host")
    private String httpProxyHost;

    @ApiModelProperty(value = "http_proxy_port")
    @TableField("http_proxy_port")
    private String httpProxyPort;

    @ApiModelProperty(value = "http_proxy_username")
    @TableField("http_proxy_username")
    private String httpProxyUsername;

    @ApiModelProperty(value = "http_proxy_password")
    @TableField("http_proxy_password")
    private String httpProxyPassword;

    @ApiModelProperty(value = "状态 1有效 0禁用")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "MD5")
    @TableField("md5")
    private String md5;

    @ApiModelProperty(value = "描述")
    @TableField("comments")
    private String comments;

}