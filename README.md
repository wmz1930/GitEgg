# GitEgg

## 项目介绍
&nbsp;&nbsp;&nbsp;&nbsp;GitEgg 是一款开源免费的企业级微服务应用开发框架，旨在整合目前主流稳定的开源技术框架，集成常用的最佳项目解决方案，实现可直接使用的微服务快速开发框架。

### 项目官网
http://gitegg.com

### Demo演示
http://demo.gitegg.com

### 系统架构
![GitEgg微服务系统架构图](https://images.gitee.com/uploads/images/2021/0622/222859_8209a5ea_378796.png "GitEgg微服务系统架构图.png")

### 功能说明
* 微服务框架组件：Spring Boot2 + SpringCloud Hoxton.SR8 + SpringCloud Alibaba
* Spring Boot Admin: 管理和监控SpringBoot应用程序的微服务健康状态
* 数据持久化组件：MySql + Druid + MyBatis + MyBatis-Plus
* Mycat: 中间件实现数据库读写分离
* Seata: 分布式事务管理，跨服务的业务操作保持数据一致性
* 高性能的key-value缓存数据库：Redis + RedissonClient + RedisTemplate
* API接口文档:  Swagger2 + knife4j
* 接口参数校验：spring-boot-starter-validation
* Nacos：一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台
* Sentinel：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性
* OpenFeign:  微服务架构下服务之间的调用的解决方案 + Ribbon实现负载均衡/高可用重试机制
* Gateway:  微服务路由转发 + 聚合knife4j微服务文档 + 【Gateway+OAuth2+JWT微服务统一认证授权】
* Oauth2：SpringSecurity单点登录功能支持多终端认证授权 + RBAC权限框架
* 验证码：集成滑动验证码【AJ-Captcha】 + 图片验证码【EasyCaptcha】
* 多租户:  基于Mybatis-Plus【TenantLineInnerInterceptor】插件实现多租户功能
* 数据权限:  基于Mybatis-Plus【DataPermissionHandler】分页插件实现可配置的数据权限功能
* 对象存储服务( OSS)：MinIO + 阿里云 + 七牛云 + 腾讯云 + 百度云 + 华为云
* 工作流：Flowable轻量级业务流程引擎
* XXL-JOB：分布式任务调度平台，作业调度系统
* Ant-design-vue + ElementUI （基础）优秀流行的前端开源框架整合
* uni-app: 可发布到iOS、Android、Web（响应式）、以及各种小程序（微信/支付宝/百度/头条/QQ/钉钉/淘宝）、快应用等多个平台 (本框架中主要用于H5、小程序)
* Flutter:  给开发者提供简单、高效的方式来构建和部署跨平台、高性能移动应用 (本框架中主要用于移动应用)
* EKL:  Elasticsearch + Logstash + Kibana分布式日志监控平台
* 代码生成器： 基于Mybatis-Plus代码生成插件开发的，便捷可配置的代码生成器
* Keepalived + Nginx: 高可用 + 高性能的HTTP和反向代理web服务器
* DevOps : kubernetes + docker + jenkins 实现持续集成（CI）和持续交付（CD）
* 数据报表：基于Ant-design-vue + Echarts实现的自定义数据可视化报表

### 目录结构

&nbsp;&nbsp;&nbsp;&nbsp;基于对敏捷开发 **L型代码结构** 的理解，以及过往研发过程中的经验，项目分为GitEgg-Platform（基础平台）和GitEgg-Cloud（业务平台）两个工程。
&nbsp;&nbsp;&nbsp;&nbsp;L型代码结构详细内容请参考： [敏捷开发“松结对编程”系列之十一：L型代码结构（团队篇之一）](https://blog.csdn.net/lancees/article/details/7914738)

* 公共平台结构 GitEgg-Platform
```
GitEgg-Platform
├── gitegg-platform-base -- GitEgg平台公共基础
├── gitegg-platform-bom -- GitEgg平台包统一管理
├── gitegg-platform-boot -- GitEgg平台SpringBoot定制模块
├── gitegg-platform-cache -- GitEgg平台缓存配置模块
├── gitegg-platform-captcha -- GitEgg平台验证码模块
├── gitegg-platform-cloud -- GitEgg平台SpringCloud定制模块
├── gitegg-platform-db -- GitEgg平台数据库链接定制模块
├── gitegg-platform-dev -- GitEgg平台代码生成基础模块
├── gitegg-platform-dfs -- GitEgg分布式文件存储接口定义
├── gitegg-platform-dfs-aliyun -- GitEgg分布式文件存储-阿里云
├── gitegg-platform-dfs-minio -- GitEgg分布式文件存储-minio
├── gitegg-platform-dfs-qiniu -- GitEgg分布式文件存储-七牛云
├── gitegg-platform-dfs-starter -- GitEgg分布式文件存储-starter（方便统一引入dfs服务）
├── gitegg-platform-dfs-tencent -- GitEgg分布式文件存储-腾讯云
├── gitegg-platform-mybatis -- GitEgg平台Mybatis自定义扩展模块
├── gitegg-platform-oauth2 -- GitEgg平台Oauth2自定义扩展模块
├── gitegg-platform-pay -- GitEgg平台统一支付模块
├── gitegg-platform-redis -- GitEgg平台Redis自定义配置模块
├── gitegg-platform-sms -- GitEgg平台短信模块接口定义
├── gitegg-platform-sms-aliyun -- GitEgg平台短信模块-阿里云
├── gitegg-platform-sms-qiniu -- GitEgg平台短信模块-七牛云
├── gitegg-platform-sms-starter -- GitEgg平台短信模块-starter（方便统一引入短信服务）
├── gitegg-platform-sms-tencent -- GitEgg平台短信模块-腾讯云
├── gitegg-platform-swagger -- GitEgg平台Swagger文档
├── gitegg-platform-push -- GitEgg平台消息推送模块
└── gitegg-platform-wechat -- GitEgg平台微信对接模块
```
* 业务平台结构 GitEgg-Cloud
```
GitEgg-Cloud
├── gitegg-common -- 公共模块工程
├── gitegg-gateway -- 网关
├── gitegg-oauth -- oauth2鉴权
└── gitegg-plugin -- 系统插件工程
     ├── gitegg-code-generator -- 代码生成模块
     ├── gitegg-flowable -- 工作流模块
     └── gitegg-xxl-job -- 定时任务模块
└── gitegg-service -- 微服务功能模块
     ├── gitegg-service-base -- 系统基础服务
     ├── gitegg-service-bigdata -- 大数据处理服务
     ├── gitegg-service-extension -- 系统服务扩展（短信、微信、分布式存储）
     └── gitegg-service-system -- 系统配置服务（权限、资源）
└── gitegg-service-client -- 微服务功能统一定义的Feign Cilent
     ├── gitegg-service-base-client -- 系统基础服务Feign Cilent
     ├── gitegg-service-bigdata-client -- 大数据处理服务Feign Cilent
     ├── gitegg-service-extension-client -- 系统服务扩展（短信、微信、分布式存储）Feign Cilent
     └── gitegg-service-system-client -- 系统配置服务（权限、资源）Feign Cilent
```
* 前端代码 GitEgg-Portal

1. gitegg-portal-ant-design 以ant-design-vue-pro为框架的后台管理界面 **（推荐使用，所有功能优先开发此版本再同步到element-ui版本）**

2. gitegg-portal-element-ui  以vue-element-admin为框架的后台管理界面 (目前只实现了登录和权限管理)

## 快速开始

### 环境准备

* 安装JDK
* 安装Maven
* 安装Mysql
* 安装Redis
* 安装Nacos
* 安装Sentinel
* 安装IntelliJ IDEA
* 安装Node.js
* 安装VSCode
* 导入后端代码
* 导入前端代码
* 导入数据库脚本
* 导入Nacos配置

### 本地运行

### 打包发布

### 项目界面
![登录界面](https://upload-images.jianshu.io/upload_images/19669137-b357fa343904018f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![用户管理](https://upload-images.jianshu.io/upload_images/19669137-44ad0680280074ac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![角色管理](https://upload-images.jianshu.io/upload_images/19669137-07f02fa112cd05d6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![资源管理](https://upload-images.jianshu.io/upload_images/19669137-16c6c9288a09c231.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![组织管理](https://upload-images.jianshu.io/upload_images/19669137-a1b6bd2a34e81b70.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![数据权限](https://upload-images.jianshu.io/upload_images/19669137-72a14c70d6ca18a8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![组织权限](https://upload-images.jianshu.io/upload_images/19669137-619c4a1c60e329b9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![数据字典](https://upload-images.jianshu.io/upload_images/19669137-e7bb90579c31d468.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![租户管理](https://upload-images.jianshu.io/upload_images/19669137-048658392907247e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![存储配置](https://upload-images.jianshu.io/upload_images/19669137-96ffd448f5db983b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![上传记录](https://upload-images.jianshu.io/upload_images/19669137-577303bc49970e48.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![短信模板](https://upload-images.jianshu.io/upload_images/19669137-9c4e2d5fdb77af0c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![短信渠道](https://upload-images.jianshu.io/upload_images/19669137-656f951da7073d32.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![操作日志](https://upload-images.jianshu.io/upload_images/19669137-6daf8e21b6e18f3f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 技术交流

## 版权许可

[License MIT](LICENSE)
