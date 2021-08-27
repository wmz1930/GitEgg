package com.gitegg.platform.mybatis.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.gitegg.platform.base.annotation.auth.DataPermission;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.domain.GitEggUser;
import com.gitegg.platform.base.exception.BusinessException;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.platform.mybatis.constant.DataPermissionConstant;
import com.gitegg.platform.mybatis.entity.DataPermissionEntity;
import com.gitegg.platform.mybatis.enums.DataPermissionTypeEnum;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 数据权限扩展
 *
 * @author GitEgg
 * @date 2021-05-12 14:25:22
 **/
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GitEggDataPermissionHandler implements DataPermissionHandler {

    @Value(("${tenant.enable}"))
    private Boolean enable;

    /**
     * 注解方式默认关闭,这里只是说明一种实现方式，实际使用时，使用配置的方式即可
     */
    @Value(("${data-permission.annotation-enable}"))
    private Boolean annotationEnable = false;

    private final RedisTemplate redisTemplate;

    public void processDataPermission(PlainSelect plainSelect, String mappedStatementId) {
        try {
            GitEggUser loginUser = GitEggAuthUtils.getCurrentUser();
            // 1 当有数据权限配置时才去判断用户是否有数据权限控制
            if (ObjectUtils.isNotEmpty(loginUser) && CollectionUtils.isNotEmpty(loginUser.getDataPermissionTypeList())) {
                // 1 根据系统配置的数据权限拼装sql
                StringBuffer statementSb = new StringBuffer();
                if (enable)
                {
                    statementSb.append(DataPermissionConstant.TENANT_DATA_PERMISSION_KEY).append(loginUser.getTenantId());
                }
                else
                {
                    statementSb.append(DataPermissionConstant.DATA_PERMISSION_KEY);
                }
                String dataPermissionKey = statementSb.toString();
                StringBuffer statementSbt = new StringBuffer(DataPermissionConstant.DATA_PERMISSION_KEY_MAPPER);
                statementSbt.append(mappedStatementId).append(DataPermissionConstant.DATA_PERMISSION_KEY_TYPE);
                String mappedStatementIdKey = statementSbt.toString();
                DataPermissionEntity dataPermissionEntity = null;
                for (String dataPermissionType: loginUser.getDataPermissionTypeList())
                {
                    String dataPermissionUserKey = mappedStatementIdKey + dataPermissionType;
                    dataPermissionEntity = (DataPermissionEntity) redisTemplate.boundHashOps(dataPermissionKey).get(dataPermissionUserKey);
                    if (ObjectUtils.isNotEmpty(dataPermissionEntity)) {
                        break;
                    }
                }
                // mappedStatementId是否有配置数据权限
                if (ObjectUtils.isNotEmpty(dataPermissionEntity))
                {
                    dataPermissionFilter(loginUser, dataPermissionEntity, plainSelect);
                }
                //默认不开启注解，因每次查询都遍历注解，影响性能，直接选择使用配置的方式实现数据权限即可
                else if(annotationEnable)
                {
                    // 2 根据注解的数据权限拼装sql
                    Class<?> clazz = Class.forName(mappedStatementId.substring(GitEggConstant.Number.ZERO, mappedStatementId.lastIndexOf(StringPool.DOT)));
                    String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + GitEggConstant.Number.ONE);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        //当有多个时，这个方法可以获取到
                        DataPermission[] annotations = method.getAnnotationsByType(DataPermission.class);
                        if (ObjectUtils.isNotEmpty(annotations) && method.getName().equals(methodName)) {
                            for (DataPermission dataPermission : annotations) {
                                String dataPermissionType = dataPermission.dataPermissionType();
                                for (String dataPermissionUser : loginUser.getDataPermissionTypeList()) {
                                    if (ObjectUtils.isNotEmpty(dataPermission) && StringUtils.isNotEmpty(dataPermissionType)
                                            && dataPermissionUser.equals(dataPermissionType)) {
                                        DataPermissionEntity dataPermissionEntityAnnotation = annotationToEntity(dataPermission);
                                        dataPermissionFilter(loginUser, dataPermissionEntityAnnotation, plainSelect);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建过滤条件
     *
     * @param user 当前登录用户
     * @param plainSelect plainSelect
     * @return 构建后查询条件
     */
    public static void dataPermissionFilter(GitEggUser user, DataPermissionEntity dataPermissionEntity, PlainSelect plainSelect) {
        Expression expression = plainSelect.getWhere();
        String dataPermissionType = dataPermissionEntity.getDataPermissionType();
        String dataTableName = dataPermissionEntity.getDataTableName();
        String dataTableAlias = dataPermissionEntity.getDataTableAlias();

        String innerTableName = StringUtils.isNotEmpty(dataPermissionEntity.getInnerTableName()) ? dataPermissionEntity.getInnerTableName(): DataPermissionConstant.DATA_PERMISSION_TABLE_NAME;
        String innerTableAlias = StringUtils.isNotEmpty(dataPermissionEntity.getInnerTableAlias()) ? dataPermissionEntity.getInnerTableAlias() : DataPermissionConstant.DATA_PERMISSION_TABLE_ALIAS_NAME;

        List<String> organizationIdList = user.getOrganizationIdList();

        // 列级数据权限
        String dataColumnExclude = dataPermissionEntity.getDataColumnExclude();
        String dataColumnInclude = dataPermissionEntity.getDataColumnInclude();
        List<String> includeColumns = new ArrayList<>();
        List<String> excludeColumns = new ArrayList<>();
        // 只包含这几个字段，也就是不是这几个字段的，直接删除
        if (StringUtils.isNotEmpty(dataColumnInclude))
        {
            includeColumns = Arrays.asList(dataColumnInclude.split(StringPool.COMMA));
        }

        // 需要排除这几个字段
        if (StringUtils.isNotEmpty(dataColumnExclude))
        {
            excludeColumns = Arrays.asList(dataColumnExclude.split(StringPool.COMMA));
        }
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        List<SelectItem> removeItems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectItems)
                && (CollectionUtils.isNotEmpty(includeColumns) || CollectionUtils.isNotEmpty(excludeColumns))) {
            for (SelectItem selectItem : selectItems) {
                // 暂不处理其他类型的selectItem
                if (selectItem instanceof SelectExpressionItem) {
                    SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
                    Alias alias = selectExpressionItem.getAlias();
                    if ((CollectionUtils.isNotEmpty(includeColumns) && !includeColumns.contains(alias.getName()))
                            || (!CollectionUtils.isEmpty(excludeColumns) && excludeColumns.contains(alias.getName())))
                    {
                        removeItems.add(selectItem);
                    }
                } else if (selectItem instanceof AllTableColumns) {
                    removeItems.add(selectItem);
                }
            }
            if (CollectionUtils.isNotEmpty(removeItems))
            {
                selectItems.removeAll(removeItems);
                plainSelect.setSelectItems(selectItems);
            }
        }

        // 行级数据权限
        // 查询用户机构和子机构的数据，这里是使用where条件添加子查询的方式来实现的，这样的实现方式好处是不需要判断Update，Insert还是Select，都是通用的，缺点是性能问题。
        if (DataPermissionTypeEnum.DATA_PERMISSION_ORG_AND_CHILD.getLevel().equals(dataPermissionType)) {
            // 如果是table的话，那么直接加inner，如果不是，那么直接在where条件里加子查询
            if (plainSelect.getFromItem() instanceof Table)
            {
                Table fromTable = (Table)plainSelect.getFromItem();
                //数据主表
                Table dataTable = null;
                //inner数据权限表
                Table innerTable = null;
                if (fromTable.getName().equalsIgnoreCase(dataTableName))
                {
                    dataTable = (Table)plainSelect.getFromItem();
                }

                // 如果是查询，这里使用inner join关联过滤，不使用子查询，因为join不需要建立临时表，因此速度比子查询快。
                List<Join> joins = plainSelect.getJoins();
                boolean hasPermissionTable = false;
                if (CollectionUtils.isNotEmpty(joins)) {
                    Iterator joinsIterator = joins.iterator();
                    while(joinsIterator.hasNext()) {
                        Join join = (Join)joinsIterator.next();
                        // 判断join里面是否存在t_sys_organization表，如果存在，那么直接使用，如果不存在则新增
                        FromItem rightItem = join.getRightItem();
                        if (rightItem instanceof Table) {
                            Table table = (Table)rightItem;
                            // 判断需要inner的主表是否存在
                            if (null == dataTable && table.getName().equalsIgnoreCase(dataTableName))
                            {
                                dataTable = table;
                            }

                            // 判断需要inner的表是否存在
                            if (table.getName().equalsIgnoreCase(innerTableName))
                            {
                                hasPermissionTable = true;
                                innerTable = table;
                            }
                        }
                    }
                }

                //如果没有找到数据主表，那么直接抛出异常
                if (null == dataTable)
                {
                    throw new BusinessException("在SQL语句中没有找到数据权限配置的主表，数据权限过滤失败。");
                }

                //如果不存在这个table，那么新增一个innerjoin
                if (!hasPermissionTable)
                {
                    innerTable = new Table(innerTableName).withAlias(new Alias(innerTableAlias, false));
                    Join join = new Join();
                    join.withRightItem(innerTable);
                    EqualsTo equalsTo = new EqualsTo();
                    equalsTo.setLeftExpression(new Column(dataTable, DataPermissionConstant.DATA_PERMISSION_ORGANIZATION_ID));
                    equalsTo.setRightExpression(new Column(innerTable, DataPermissionConstant.DATA_PERMISSION_ID));
                    join.withOnExpression(equalsTo);
                    plainSelect.addJoins(join);
                }

                EqualsTo equalsToWhere = new EqualsTo();
                equalsToWhere.setLeftExpression(new Column(innerTable, DataPermissionConstant.DATA_PERMISSION_ID));
                equalsToWhere.setRightExpression(new LongValue(user.getOrganizationId()));
                Function function = new Function();
                function.setName(DataPermissionConstant.DATA_PERMISSION_FIND_IN_SET);
                function.setParameters(new ExpressionList(new LongValue(user.getOrganizationId()) , new Column(innerTable, DataPermissionConstant.DATA_PERMISSION_ANCESTORS)));
                OrExpression orExpression = new OrExpression(equalsToWhere, function);
                //判断是否有数据权限，如果有数据权限配置，那么添加数据权限的机构列表
                if(CollectionUtils.isNotEmpty(organizationIdList))
                {
                    for (String organizationId : organizationIdList)
                    {
                        EqualsTo equalsToPermission = new EqualsTo();
                        equalsToPermission.setLeftExpression(new Column(innerTable, DataPermissionConstant.DATA_PERMISSION_ID));
                        equalsToPermission.setRightExpression(new LongValue(organizationId));
                        orExpression = new OrExpression(orExpression, equalsToPermission);
                        Function functionPermission = new Function();
                        functionPermission.setName(DataPermissionConstant.DATA_PERMISSION_FIND_IN_SET);
                        functionPermission.setParameters(new ExpressionList(new LongValue(organizationId) , new Column(innerTable,DataPermissionConstant.DATA_PERMISSION_ANCESTORS)));
                        orExpression = new OrExpression(orExpression, functionPermission);
                    }
                }
                expression = ObjectUtils.isNotEmpty(expression) ? new AndExpression(expression, new Parenthesis(orExpression)) : orExpression;
                plainSelect.setWhere(expression);
            }
            else
            {
                InExpression inExpression = new InExpression();
                inExpression.setLeftExpression(buildColumn(dataTableAlias, DataPermissionConstant.DATA_PERMISSION_ORGANIZATION_ID));
                SubSelect subSelect = new SubSelect();
                PlainSelect select = new PlainSelect();
                select.setSelectItems(Collections.singletonList(new SelectExpressionItem(new Column(DataPermissionConstant.DATA_PERMISSION_ID))));
                select.setFromItem(new Table(DataPermissionConstant.DATA_PERMISSION_TABLE_NAME));
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column(DataPermissionConstant.DATA_PERMISSION_ID));
                equalsTo.setRightExpression(new LongValue(user.getOrganizationId()));
                Function function = new Function();
                function.setName(DataPermissionConstant.DATA_PERMISSION_FIND_IN_SET);
                function.setParameters(new ExpressionList(new LongValue(user.getOrganizationId()) , new Column(DataPermissionConstant.DATA_PERMISSION_ANCESTORS)));
                OrExpression orExpression = new OrExpression(equalsTo, function);

                //判断是否有数据权限，如果有数据权限配置，那么添加数据权限的机构列表
                if(CollectionUtils.isNotEmpty(organizationIdList))
                {
                    for (String organizationId : organizationIdList)
                    {
                        EqualsTo equalsToPermission = new EqualsTo();
                        equalsToPermission.setLeftExpression(new Column(DataPermissionConstant.DATA_PERMISSION_ID));
                        equalsToPermission.setRightExpression(new LongValue(organizationId));
                        orExpression = new OrExpression(orExpression, equalsToPermission);
                        Function functionPermission = new Function();
                        functionPermission.setName(DataPermissionConstant.DATA_PERMISSION_FIND_IN_SET);
                        functionPermission.setParameters(new ExpressionList(new LongValue(organizationId) , new Column(DataPermissionConstant.DATA_PERMISSION_ANCESTORS)));
                        orExpression = new OrExpression(orExpression, functionPermission);
                    }
                }
                select.setWhere(orExpression);
                subSelect.setSelectBody(select);
                inExpression.setRightExpression(subSelect);
                expression = ObjectUtils.isNotEmpty(expression) ? new AndExpression(expression, new Parenthesis(inExpression)) : inExpression;
                plainSelect.setWhere(expression);
            }
        }
        // 只查询用户拥有机构的数据，不包含子机构
        else if (DataPermissionTypeEnum.DATA_PERMISSION_ORG.getLevel().equals(dataPermissionType)) {
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(buildColumn(dataTableAlias, DataPermissionConstant.DATA_PERMISSION_ORGANIZATION_ID));
            ExpressionList expressionList = new ExpressionList();
            List<Expression> expressions = new ArrayList<>();
            expressions.add(new LongValue(user.getOrganizationId()));
            if(CollectionUtils.isNotEmpty(organizationIdList))
            {
                for (String organizationId : organizationIdList)
                {
                    expressions.add(new LongValue(organizationId));
                }
            }
            expressionList.setExpressions(expressions);
            inExpression.setRightItemsList(expressionList);
            expression = ObjectUtils.isNotEmpty(expression) ? new AndExpression(expression, new Parenthesis(inExpression)) : inExpression;
            plainSelect.setWhere(expression);

        }
        // 只能查询个人数据
        else if (DataPermissionTypeEnum.DATA_PERMISSION_SELF.getLevel().equals(dataPermissionType)) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(buildColumn(dataTableAlias, DataPermissionConstant.DATA_PERMISSION_SELF));
            equalsTo.setRightExpression(new StringValue(String.valueOf(user.getId())));
            expression = ObjectUtils.isNotEmpty(expression) ? new AndExpression(expression, new Parenthesis(equalsTo)) : equalsTo;
            plainSelect.setWhere(expression);
        }
        //当类型为查看所有数据时，不处理
//        if (DataPermissionTypeEnum.DATA_PERMISSION_ALL.getType().equals(dataPermissionType)) {
//
//        }
        // 自定义过滤语句
        else if (DataPermissionTypeEnum.DATA_PERMISSION_CUSTOM.getLevel().equals(dataPermissionType)) {
            String customExpression = dataPermissionEntity.getCustomExpression();
            if (StringUtils.isEmpty(customExpression))
            {
                throw new BusinessException("没有配置自定义表达式");
            }
            try {
                Expression expressionCustom = CCJSqlParserUtil.parseCondExpression(customExpression);
                expression = ObjectUtils.isNotEmpty(expression) ? new AndExpression(expression, new Parenthesis(expressionCustom)) : expressionCustom;
                plainSelect.setWhere(expression);
            } catch (JSQLParserException e) {
                throw new BusinessException("自定义表达式配置错误");
            }
        }
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
            columnName = dataTableAlias + StringPool.DOT + columnName;
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
        dataPermissionEntity.setDataPermissionType(annotation.dataPermissionType());
        dataPermissionEntity.setDataColumnExclude(annotation.dataColumnExclude());
        dataPermissionEntity.setDataColumnInclude(annotation.dataColumnInclude());
        dataPermissionEntity.setDataTableName(annotation.dataTableName());
        dataPermissionEntity.setDataTableAlias(annotation.dataTableAlias());
        dataPermissionEntity.setInnerTableName(annotation.innerTableName());
        dataPermissionEntity.setInnerTableAlias(annotation.innerTableAlias());
        dataPermissionEntity.setCustomExpression(annotation.customExpression());
        return dataPermissionEntity;
    }

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        return null;
    }
}
