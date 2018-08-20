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
@ConfigurationProperties(prefix="druid.monitor")
public class DruidMonitorSpringProperties {

	private String[] serviceAndDaoId;
	
}
