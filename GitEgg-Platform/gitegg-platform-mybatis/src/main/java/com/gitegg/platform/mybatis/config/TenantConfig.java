package com.gitegg.platform.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.gitegg.platform.boot.util.GitEggAuthUtils;
import com.gitegg.platform.mybatis.props.TenantProperties;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 多租户配置中心
 *
 * @author GitEgg
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureBefore(MybatisPlusConfig.class)
public class TenantConfig {

	private final TenantProperties tenantProperties;

	/**
	 * 新多租户插件配置,一缓和二缓遵循mybatis的规则,
	 * 需要设置 MybatisConfiguration#useDeprecatedExecutor = false
	 * 避免缓存万一出现问题
	 *
	 * @return TenantLineInnerInterceptor
	 */
	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
		return new TenantLineInnerInterceptor(new TenantLineHandler() {
			/**
			 * 获取租户ID
			 * @return Expression
			 */
			@Override
			public Expression getTenantId() {
				String tenant = GitEggAuthUtils.getTenantId();
				if (tenant != null) {
					return new StringValue(GitEggAuthUtils.getTenantId());
				}
				return new NullValue();
			}

			/**
			 * 获取多租户的字段名
			 * @return String
			 */
			@Override
			public String getTenantIdColumn() {
				return tenantProperties.getColumn();
			}

			/**
			 * 过滤不需要根据租户隔离的表
			 * 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
			 * @param tableName 表名
			 */
			@Override
			public boolean ignoreTable(String tableName) {
				return tenantProperties.getExclusionTable().stream().anyMatch(
						(t) -> t.equalsIgnoreCase(tableName)
				);
			}
		});
	}
}
