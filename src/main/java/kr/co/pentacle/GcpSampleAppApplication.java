package kr.co.pentacle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.autoconfigure.sql.CloudSqlJdbcInfoProvider;
import org.springframework.cloud.gcp.autoconfigure.sql.GcpCloudSqlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GcpSampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcpSampleAppApplication.class, args);
	}
	
	@Bean
	@Profile("local")
	public CloudSqlJdbcInfoProvider h2CloudSqlJdbcInfoProvider(GcpCloudSqlProperties properties) {
		return new H2CloudSqlJdbcInfoProvider(properties);
	}
	
}
