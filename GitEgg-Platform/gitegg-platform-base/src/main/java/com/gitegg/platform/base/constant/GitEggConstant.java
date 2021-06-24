package com.gitegg.platform.base.constant;

/**
 * @ClassName: GitEggConstant
 * @Description: 常量类
 * @author gitegg
 * @date 2019年5月18日 下午11:49:21
 */
public class GitEggConstant {

    /**
     * 根节点id
     */
    public static final Long PARENT_ID = 0L;

    /**
     * 启用
     */
    public static final int ENABLE = 1;

    /**
     * 禁用
     */
    public static final int DISABLE = 0;

    /**
     * 结果返回0
     */
    public static final int COUNT_ZERO = 0;

    /**
     * 数组取值
     */
    public class Number {

        public static final int ZERO = 0;

        public static final int ONE = 1;

        public static final int TWO = 2;

        public static final int THREE = 3;

        public static final int FOUR = 4;

        public static final int FIVE = 5;

        public static final int FIFTEEN = 15;

        public static final int HUNDRED = 100;

        public static final int THOUSAND = 1000;
    }

    /**
     * 地址
     */
    public class Address {

        public static final int PROVINCE = 0;

        public static final int CITY = 1;

        public static final int AREA = 2;

    }

    /**
     * 超时时间类型
     */
    public class ExpTimeType {

        public static final String WEB = "web";

        public static final String APP = "app";

    }
}
