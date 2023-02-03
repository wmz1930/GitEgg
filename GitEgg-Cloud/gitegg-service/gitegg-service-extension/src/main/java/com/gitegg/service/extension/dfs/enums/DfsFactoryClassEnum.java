package com.gitegg.service.extension.dfs.enums;


/**
 * @ClassName: DfsFactoryClassEnum
 * @Description: 分布式存储工厂类枚举 ，因dfs表存的是数据字典表的id，这里省一次数据库查询，所以就用数据字典的id
 * @author GitEgg
 * @date 2020年09月19日 下午11:49:45
 */
public enum DfsFactoryClassEnum {

    /**
     * MINIO MINIO
     */
    MINIO("MINIO", "com.gitegg.service.extension.dfs.factory.DfsMinioFactory"),

    /**
     * 七牛云Kodo QINIUYUN_KODO
     */
    QI_NIU("QINIUYUN_KODO", "com.gitegg.service.extension.dfs.factory.DfsQiniuFactory"),

    /**
     * 阿里云OSS ALIYUN_OSS
     */
    ALI_YUN("ALIYUN_OSS", "com.gitegg.service.extension.dfs.factory.DfsAliyunFactory"),

    /**
     * 腾讯云COS TENCENT_COS
     */
    TENCENT("TENCENT_COS", "com.gitegg.service.extension.dfs.factory.DfsTencentFactory");

    public String code;

    public String value;

    DfsFactoryClassEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(String code) {
        DfsFactoryClassEnum[] smsFactoryClassEnums = values();
        for (DfsFactoryClassEnum smsFactoryClassEnum : smsFactoryClassEnums) {
            if (smsFactoryClassEnum.getCode().equals(code)) {
                return smsFactoryClassEnum.getValue();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
