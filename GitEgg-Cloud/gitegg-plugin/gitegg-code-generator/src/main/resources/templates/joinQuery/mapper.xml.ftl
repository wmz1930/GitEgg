<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 连接查询通用连接查询映射结果 -->
    <resultMap id="${entity}BaseResultMap" type="${package.Entity?replace("entity","dto")}.${entity}DTO">
<#list table.fields as field>
    <#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
    </#if>
</#list>
<#list table.fields as field>
    <#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
    </#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list joinFields as field><#--生成连接表字段 -->
        <result column="${field.fieldName}" property="${field.entityName}" />
</#list>
    </resultMap>
</#if>
<#if baseColumnList>

    <!-- 联合字表查询结果列 -->
    <sql id="${entity}Base_Column_List">
        <#list table.fields as field> ${config.tableAlias}.${field.name} as ${field.name}<#if field?has_next && joinFields?? && (joinFields?size > 0)>,</#if></#list><#if joinFields?? && (joinFields?size > 0)><#list joinFields as field> ${field.tableAlias}.${field.fieldName} as ${field.entityName}<#if field?has_next>,</#if></#list></#if>
    </sql>

</#if>
<#macro JoinType type>
    <#if type == "left">
        LEFT JOIN
    <#elseif type == "right">
        RIGHT JOIN
    <#elseif type == "right">
        RIGHT JOIN
    <#elseif type == "inner">
        INNER JOIN
    <#elseif type == "union">
        UNION
    </#if>
</#macro>
<#assign dtoPackage="${package.Entity}"/>
    <!-- 分页查询${table.comment!}列表 -->
    <select id="query${entity}List" resultType="${dtoPackage?replace("entity","dto")}.${entity}DTO" parameterType="${dtoPackage?replace("entity","dto")}.Query${entity}DTO">
        SELECT
        <include refid="${entity}Base_Column_List"/>
        FROM ${table.name} ${config.tableAlias}
        <#list joinTables as tableJoin><#--生成连接表字段 -->
<@JoinType type="${tableJoin.joinTableType}"/> ${tableJoin.joinTableName} ${tableJoin.joinTableAlias} ON ${tableJoin.joinTableAlias}.del_flag = 0<#if tableJoin.associationId??> and ${config.tableAlias}.id = ${tableJoin.joinTableAlias}.${tableJoin.associationId}</#if><#if tableJoin.joinTableOn?exists && tableJoin.joinTableOn != ""> and ${tableJoin.joinTableOn}</#if>
        </#list>
        WHERE ${config.tableAlias}.del_flag = 0
        <#-- ----------  BEGIN 字段循环遍历 ---------->
        <#list fields as field>
            <#if field?? && field.queryTerm == true>
                <#if field?? && field.queryType == "EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} = <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "NOT_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} != <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "GREATER">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &gt; <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "GREATER_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &gt;= <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LESS">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &lt; <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LESS_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &lt;= <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LIKE">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat('%', <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>, '%')
                    </if>
                <#elseif field?? && field.queryType == "LIKE_LEFT">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat(<#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>, '%')
                    </if>
                <#elseif field?? && field.queryType == "LIKE_RIGHT">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat('%', <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>)
                    </if>
                </#if>
            </#if>
        </#list>
        <#------------  END 字段循环遍历  ---------->
        <if test="${table.entityPath}.beginDateTime != null and ${table.entityPath}.beginDateTime != ''">
            AND DATE_FORMAT(${config.tableAlias}.create_time,'%Y-%m-%d %H:%M:%S') &gt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.beginDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        <if test="${table.entityPath}.endDateTime != null and ${table.entityPath}.endDateTime != ''">
            AND DATE_FORMAT(${config.tableAlias}.create_time,'%Y-%m-%d %H:%M:%S') &lt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.endDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        ORDER BY ${config.tableAlias}.id DESC
    </select>
    <#if config?? && config.queryReuse == false>

    <!-- 查询${table.comment!}信息 -->
    <select id="query${entity}" resultType="${dtoPackage?replace("entity","dto")}.${entity}DTO" parameterType="${dtoPackage?replace("entity","dto")}.Query${entity}DTO">
        SELECT
        <include refid="${entity}Base_Column_List"/>
        FROM ${table.name} ${config.tableAlias}
        <#list joinTables as tableJoin><#--生成连接表字段 -->
            <@JoinType type="${tableJoin.joinTableType}"/> ${tableJoin.joinTableName} ${tableJoin.joinTableAlias} ON ${tableJoin.joinTableAlias}.del_flag = 0<#if tableJoin.associationId??> and ${config.tableAlias}.id = ${tableJoin.joinTableAlias}.${tableJoin.associationId}</#if><#if tableJoin.joinTableOn?exists && tableJoin.joinTableOn != ""> and ${tableJoin.joinTableOn}</#if>
        </#list>
        WHERE ${config.tableAlias}.del_flag = 0
        <#-- ----------  BEGIN 字段循环遍历 ---------->
        <#list fields as field>
            <#if field?? && field.queryTerm == true>
                <#if field?? && field.queryType == "EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} = <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "NOT_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} != <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "GREATER">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &gt; <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "GREATER_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &gt;= <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LESS">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &lt; <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LESS_EQUAL">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} &lt;= <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>
                    </if>
                <#elseif field?? && field.queryType == "LIKE">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat('%', <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>, '%')
                    </if>
                <#elseif field?? && field.queryType == "LIKE_LEFT">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat(<#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>, '%')
                    </if>
                <#elseif field?? && field.queryType == "LIKE_RIGHT">
                    <if test="${table.entityPath}.${field.entityName} != null and ${table.entityPath}.${field.entityName} != ''">
                        AND ${joinTableAlias}.${field.fieldName} like concat('%', <#noparse>#{</#noparse>${table.entityPath}.${field.entityName}<#noparse>}</#noparse>)
                    </if>
                </#if>
            </#if>
        </#list>
        <#------------  END 字段循环遍历  ---------->
        <if test="${table.entityPath}.beginDateTime != null and ${table.entityPath}.beginDateTime != ''">
            AND DATE_FORMAT(${config.tableAlias}.create_time,'%Y-%m-%d %H:%M:%S') &gt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.beginDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        <if test="${table.entityPath}.endDateTime != null and ${table.entityPath}.endDateTime != ''">
            AND DATE_FORMAT(${config.tableAlias}.create_time,'%Y-%m-%d %H:%M:%S') &lt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.endDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        ORDER BY ${config.tableAlias}.id DESC
    </select>
    </#if>
</mapper>
