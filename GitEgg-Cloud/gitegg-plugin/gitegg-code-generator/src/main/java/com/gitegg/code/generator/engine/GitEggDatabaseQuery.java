/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gitegg.code.generator.engine;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.IDatabaseQuery;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.DecoratorDbQuery;
import com.gitegg.code.generator.engine.dto.TableDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nieqiurong 2021/1/6.
 * @since 3.5.0
 */
public class GitEggDatabaseQuery extends IDatabaseQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitEggDatabaseQuery.class);

    private final StrategyConfig strategyConfig;

    private final GlobalConfig globalConfig;

    private final DecoratorDbQuery dbQuery;

    public GitEggDatabaseQuery(ConfigBuilder configBuilder) {
        super(configBuilder);
        this.strategyConfig = configBuilder.getStrategyConfig();
        this.dbQuery = new DecoratorDbQuery(dataSourceConfig, strategyConfig);
        this.globalConfig = configBuilder.getGlobalConfig();
    }

    @Override
    public List<TableInfo> queryTables() {
        return null;
    }

    public List<TableDTO> queryDatasourceTables() {
        //所有的表信息
        List<TableDTO> tableList = new ArrayList<>();
        try {
            dbQuery.query(dbQuery.tablesSql(), result -> {
                String tableName = result.getStringResult(dbQuery.tableName());
                String tableComment = result.getTableComment();
                if (StringUtils.isNotBlank(tableName) && !"VIEW".equals(tableComment)) {
                    TableDTO tableDTO = new TableDTO();
                    tableDTO.setTableName(tableName);
                    tableDTO.setTableComment(tableComment);
                    tableList.add(tableDTO);
                }
            });
            return tableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 数据库操作完成,释放连接对象
            dbQuery.closeConnection();
        }
    }
}
