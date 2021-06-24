package com.gitegg.platform.mybatis.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.gitegg.platform.base.annotation.auth.DataPermission;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.platform.mybatis.constant.DataPermissionConstant;
import com.gitegg.platform.mybatis.entity.DataPermissionEntity;
import com.gitegg.platform.mybatis.enums.DataPermissionTypeEnum;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

/**
 * 数据权限扩展
 *
 * @author GitEgg
 * @date 2021-05-12 14:25:22
 **/
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggDataPermissionHandler implements DataPermissionHandler {

    private final RedisTemplate redisTemplate;

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        try {

            // 1 根据系统配置的数据权限拼装sql
            StringBuffer statementSb = new StringBuffer();
            statementSb.append(DataPermissionConstant.DATA_PERMISSION_KEY).append(DataPermissionConstant.DATA_PERMISSION_KEY_MAPPER)
                    .append(mappedStatementId);
            String mappedStatementIdKey = statementSb.toString();
            String mappedRoleKey = statementSb.append(DataPermissionConstant.DATA_PERMISSION_KEY_TYPE).toString();
            // 获取缓存中是否有mappedStatementId的key
            Set<String> keys = redisTemplate.keys(mappedStatementIdKey + DataPermissionConstant.DATA_PERMISSION_KEY_REGULAR_EXPRESSION);
            // 获取当前的用户
            // 如果mappedStatementId没有配置数据权限规则，则直接跳过
            // 这里的判断和下面的User判断分开，主要是考虑到并不是每个mapper都会有数据权限判断，尽量减少GitEggUser的序列化操作
            if (CollectionUtils.isNotEmpty(keys)) {
                //当有数据权限配置时才去判断用户是否有数据权限控制
                GitEggUser loginUser = GitEggAuthUtils.getCurrentUser();
                if (ObjectUtils.isNotEmpty(loginUser) && StringUtils.isNotEmpty(loginUser.getDataPermissionType())) {
                    //首先判断key是否存在再去缓存取
                    if (keys.contains(mappedRoleKey + loginUser.getDataPermissionType()))
                    {
                       //查询根据mappedStatementId是否有配置数据权限
                       String dataPermissionJson = (String)redisTemplate.opsForValue().get(mappedRoleKey + loginUser.getDataPermissionType());
                       //同一个mapper同一个数据权限类型只允许有一个配置
                       DataPermissionEntity dataPermissionEntity = JSONUtil.toBean(dataPermissionJson, DataPermissionEntity.class);
                       where = dataPermissionTypeFilter(loginUser, dataPermissionEntity, where);
                   }
                }
            }

            // 2 根据注解的数据权限拼装sql,这里需要优化，注解的权限需要支持多种DataPermissionType类型，目前只支持一种
            Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(".")));
            String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                DataPermission annotation = method.getAnnotation(DataPermission.class);
                if (ObjectUtils.isNotEmpty(annotation) && (method.getName().equals(methodName) || (method.getName() + "_COUNT").equals(methodName))) {
                    // 获取当前的用户
                    GitEggUser loginUser = GitEggAuthUtils.getCurrentUser();
                    String dataPermissionType = annotation.dataPermissionType();
                    if (ObjectUtils.isNotEmpty(loginUser) && StringUtils.isNotEmpty(loginUser.getDataPermissionType())
                    && loginUser.getDataPermissionType().equals(dataPermissionType)) {
                        DataPermissionEntity dataPermissionEntity = annotationToEntity(annotation);
                        where = dataPermissionTypeFilter(loginUser, dataPermissionEntity, where);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return where;
    }


    /**
     * 构建过滤条件
     *
     * @param user 当前登录用户
     * @param where 当前查询条件
     * @return 构建后查询条件
     */
    public static Expression dataPermissionTypeFilter(GitEggUser user, DataPermissionEntity dataPermissionEntity, Expression where) {
        Expression expression = null;
        String dataPermissionType = dataPermissionEntity.getDataPermissionType();
        String dataTableAlias = dataPermissionEntity.getDataTableAlias();
        if (DataPermissionTypeEnum.DATA_PERMISSION_ALL.getType().equals(dataPermissionType)) {
            return where;
        }
        // TODO 暂不支持自定义，可根据需求扩展
        if (DataPermissionTypeEnum.DATA_PERMISSION_CUSTOM.equals(dataPermissionType)) {
            return where;
//            InExpression inExpression = new InExpression();
//            inExpression.setLeftExpression(buildColumn(dataTableAlias, "organization_id"));
//            SubSelect subSelect = new SubSelect();
//            PlainSelect select = new PlainSelect();
//            select.setSelectItems(Collections.singletonList(new SelectExpressionItem(new Column("organization_id"))));
//            select.setFromItem(new Table(""));
//            EqualsTo equalsTo = new EqualsTo();
//            equalsTo.setLeftExpression();
//            equalsTo.setRightExpression();
//            select.setWhere(equalsTo);
//            subSelect.setSelectBody(select);
//            inExpression.setRightExpression(subSelect);
//            expression = ObjectUtils.isNotEmpty(expression) ? new OrExpression(expression, inExpression) : inExpression;
        }
        // TODO 机构表需要增加ancestors字段，用于方便查找父级所有子机构
        if (DataPermissionTypeEnum.DATA_PERMISSION_ORG_AND_CHILD.equals(dataPermissionType)) {
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(buildColumn(dataTableAlias, "organization_id"));
            SubSelect subSelect = new SubSelect();
            PlainSelect select = new PlainSelect();
            select.setSelectItems(Collections.singletonList(new SelectExpressionItem(new Column("organization_id"))));
            select.setFromItem(new Table("t_sys_organization"));
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column("organization_id"));
            equalsTo.setRightExpression(new LongValue(user.getOrganizationId()));
            Function function = new Function();
            function.setName("find_in_set");
            function.setParameters(new ExpressionList(new LongValue(user.getOrganizationId()) , new Column("ancestors")));
            select.setWhere(new OrExpression(equalsTo, function));
            subSelect.setSelectBody(select);
            inExpression.setRightExpression(subSelect);
            expression = ObjectUtils.isNotEmpty(expression) ? new OrExpression(expression, inExpression) : inExpression;
        }
        // TODO这里需要改为获取用户的数据权限机构
        if (DataPermissionTypeEnum.DATA_PERMISSION_ORG.equals(dataPermissionType)) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(buildColumn(dataTableAlias, "organization_id"));
            equalsTo.setRightExpression(new LongValue(user.getOrganizationId()));
            expression = ObjectUtils.isNotEmpty(expression) ? new OrExpression(expression, equalsTo) : equalsTo;
        }
        if (DataPermissionTypeEnum.DATA_PERMISSION_SELF.equals(dataPermissionType)) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(buildColumn(dataTableAlias, "creator"));
            equalsTo.setRightExpression(new StringValue(String.valueOf(user.getId())));
            expression = ObjectUtils.isNotEmpty(expression) ? new OrExpression(expression, equalsTo) : equalsTo;
        }

        return ObjectUtils.isNotEmpty(where) ? new AndExpression(where, new Parenthesis(expression)) : expression;
    }

    /**
     * 构建Column
     *
     * @param dataTableAlias 表别名
     * @param columnName 字段名称
     * @return 带表别名字段
     */
    public static Column buildColumn(String dataTableAlias, String columnName) {
        if (StringUtils.isNotEmpty(dataTableAlias)) {
            columnName = dataTableAlias + "." + columnName;
        }
        return new Column(columnName);
    }


    /**
     * 注解转为实体类
     * @param annotation 注解
     * @return 实体类
     */
    public static DataPermissionEntity annotationToEntity(DataPermission annotation) {
        DataPermissionEntity dataPermissionEntity = new DataPermissionEntity();
        if (StringUtils.isNotEmpty(annotation.dataPermissionType())) {
            dataPermissionEntity.setDataPermissionType(annotation.dataPermissionType());
        }
        dataPermissionEntity.setDataColumnExclude(annotation.dataColumnExclude());
        dataPermissionEntity.setDataColumnInclude(annotation.dataColumnInclude());
        dataPermissionEntity.setDataTableName(annotation.dataTableName());
        dataPermissionEntity.setDataTableAlias(annotation.dataTableAlias());
        dataPermissionEntity.setInnerTableName(annotation.innerTableName());
        dataPermissionEntity.setInnerTableAlias(annotation.innerTableAlias());
        return dataPermissionEntity;
    }
}
