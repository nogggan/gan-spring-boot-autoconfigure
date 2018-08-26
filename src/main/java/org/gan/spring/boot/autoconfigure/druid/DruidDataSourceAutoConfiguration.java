package org.gan.spring.boot.autoconfigure.druid;

import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(value=DataSourceAutoConfiguration.class)
@ConditionalOnClass(value={DruidDataSource.class,DataSource.class,EmbeddedDatabaseType.class})
@EnableConfigurationProperties(value= {DruidViewProperties.class,DataSourceProperties.class,DruidMonitorSpringProperties.class})
public class DruidDataSourceAutoConfiguration {
	
	@Bean(initMethod="init",destroyMethod="close",name="druidDataSource")
	@ConditionalOnMissingBean(value=DataSource.class)
	@ConfigurationProperties(prefix="spring.druid")
	public DataSource dataSource(DataSourceProperties properties,StatFilter stat) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(properties.getUsername());
		dataSource.setPassword(properties.getPassword());
		dataSource.setUrl(properties.getUrl());
		dataSource.setDriverClassName(properties.getDriverClassName());
		dataSource.setFilters("stat,wall");
		dataSource.setProxyFilters(Arrays.asList(stat));
		return dataSource;
	}
	
	@Bean
	public StatFilter stat(DruidViewProperties properties) {
		StatFilter statFilter = new StatFilter();
		statFilter.setSlowSqlMillis(properties.getSlowSqlMillis());
		statFilter.setLogSlowSql(properties.isLogSlowSql());
		return statFilter;
	}
	
	@Bean
	@ConditionalOnBean(name="druidDataSource")
	public ServletRegistrationBean<StatViewServlet> statServlet(DruidViewProperties properties){
		ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>();
		StatViewServlet servlet = new StatViewServlet();
		registrationBean.setServlet(servlet);
		registrationBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, properties.getServletUsername());
		registrationBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, properties.getServletPassword());
		registrationBean.addUrlMappings("/druid/*");
		return registrationBean;
	}
	
	@Bean
	@ConditionalOnBean(name="druidDataSource")
	public FilterRegistrationBean<WebStatFilter> statFilter(DruidViewProperties properties){
		FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<>();
		WebStatFilter filter = new WebStatFilter();
		registrationBean.setFilter(filter);
		registrationBean.addInitParameter(WebStatFilter.PARAM_NAME_EXCLUSIONS, properties.getFilterExclusions());
		registrationBean.addUrlPatterns(properties.getFilterUrlPattern());
		return registrationBean;
	}
	
	@Bean(name="druidStatInterceptor")
	@ConditionalOnBean(name="druidDataSource")
	public DruidStatInterceptor druidStatInterceptor() {
		DruidStatInterceptor interceptor = new DruidStatInterceptor();
		return interceptor;
	}
	
	@Bean
	@ConditionalOnBean(name="druidStatInterceptor")
	public BeanNameAutoProxyCreator creator(DruidMonitorSpringProperties properties) {
		BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
		creator.setProxyTargetClass(true);
		creator.setInterceptorNames("druidStatInterceptor");
		String[] beanNames = properties.getServiceAndDaoId();
		if(beanNames != null)
			creator.setBeanNames(properties.getServiceAndDaoId());
		return creator;
	}

}
