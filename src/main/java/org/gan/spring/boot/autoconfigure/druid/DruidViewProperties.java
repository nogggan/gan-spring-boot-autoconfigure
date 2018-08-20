package org.gan.spring.boot.autoconfigure.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ConfigurationProperties(prefix="druid.view")
public class DruidViewProperties {
	
	private String servletUsername = "";
	
	private String servletPassword = "";
	
	private String servletUrlPattern = "/druid/*";
	
	private String filterExclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";
	
	private String filterUrlPattern = "/*";
	
	private long slowSqlMillis = 10000;
	
	private boolean logSlowSql = true;

}
