package com.gitegg.platform.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName: MyMetaObjectHandler
 * @Description: 自定义填充公共字段
 * @author gitegg
 * @date 2019年5月19日 上午10:32:29
 */
@Component
public class GitEggMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object creator = getFieldValByName("creator", metaObject);
        GitEggUser gitEggUser = GitEggAuthUtils.getCurrentUser();
        if (null == creator && null != gitEggUser) {
           setFieldValByName("creator", gitEggUser.getId(), metaObject);
        }
        Object createTime = getFieldValByName("createTime", metaObject);
        if (null == createTime) {
            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object operator = getFieldValByName("operator", metaObject);
        GitEggUser gitEggUser = GitEggAuthUtils.getCurrentUser();
        if (null == operator && null != gitEggUser) {
            setFieldValByName("operator", gitEggUser.getId(), metaObject);
        }
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (null == updateTime) {
            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}
