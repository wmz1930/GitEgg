package com.gitegg.platform.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.enums.BaseEntityEnum;
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
        Object creator = getFieldValByName(BaseEntityEnum.CREATOR.entity, metaObject);
        GitEggUser gitEggUser = GitEggAuthUtils.getCurrentUser();
        if (null == creator && null != gitEggUser) {
           setFieldValByName(BaseEntityEnum.CREATOR.entity, gitEggUser.getId(), metaObject);
        }
        Object createTime = getFieldValByName(BaseEntityEnum.CREATE_TIME.entity, metaObject);
        if (null == createTime) {
            setFieldValByName(BaseEntityEnum.CREATE_TIME.entity, LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object operator = getFieldValByName(BaseEntityEnum.OPERATOR.entity, metaObject);
        GitEggUser gitEggUser = GitEggAuthUtils.getCurrentUser();
        if (null == operator && null != gitEggUser) {
            setFieldValByName(BaseEntityEnum.OPERATOR.entity, gitEggUser.getId(), metaObject);
        }
        Object updateTime = getFieldValByName(BaseEntityEnum.UPDATE_TIME.entity, metaObject);
        if (null == updateTime) {
            setFieldValByName(BaseEntityEnum.UPDATE_TIME.entity, LocalDateTime.now(), metaObject);
        }
    }
}
