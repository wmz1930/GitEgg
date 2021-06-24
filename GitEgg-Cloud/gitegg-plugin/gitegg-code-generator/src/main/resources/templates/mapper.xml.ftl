<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
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
    </resultMap>
</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        ${table.fieldNames},<#list table.commonFields as field> ${field.columnName}<#if field?has_next>,</#if></#list>
    </sql>

</#if>
<#assign dtoPackage="${package.Entity}"/>
    <!-- 分页查询${table.comment!}列表 -->
    <select id="query${entity}List" resultType="${dtoPackage?replace("entity","dto")}.${entity}DTO" parameterType="${dtoPackage?replace("entity","dto")}.Query${entity}DTO">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${table.name}
        WHERE del_flag = 0
        <if test="${table.entityPath}.id != null and ${table.entityPath}.id != ''">
            AND id = <#noparse>#{</#noparse>${table.entityPath}.id<#noparse>}</#noparse>
        </if>
        <if test="${table.entityPath}.startDateTime != null and ${table.entityPath}.startDateTime != ''">
            AND DATE_FORMAT(create_time,'%Y-%m-%d %H:%M:%S') &gt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.startDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        <if test="${table.entityPath}.endDateTime != null and ${table.entityPath}.endDateTime != ''">
            AND DATE_FORMAT(create_time,'%Y-%m-%d %H:%M:%S') &lt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.endDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        ORDER BY id DESC
    </select>

    <!-- 查询${table.comment!}信息 -->
    <select id="query${entity}" resultType="${dtoPackage?replace("entity","dto")}.${entity}DTO" parameterType="${dtoPackage?replace("entity","dto")}.Query${entity}DTO">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${table.name}
        WHERE del_flag = 0
        <if test="${table.entityPath}.id != null and ${table.entityPath}.id != ''">
            AND id = <#noparse>#{</#noparse>${table.entityPath}.id<#noparse>}</#noparse>
        </if>
        <if test="${table.entityPath}.startDateTime != null and ${table.entityPath}.startDateTime != ''">
            AND DATE_FORMAT(create_time,'%Y-%m-%d %H:%M:%S') &gt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.startDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        <if test="${table.entityPath}.endDateTime != null and ${table.entityPath}.endDateTime != ''">
            AND DATE_FORMAT(create_time,'%Y-%m-%d %H:%M:%S') &lt;= DATE_FORMAT(<#noparse>#{</#noparse>${table.entityPath}.endDateTime<#noparse>}</#noparse>,'%Y-%m-%d %H:%M:%S')
        </if>
        ORDER BY id DESC
    </select>

</mapper>