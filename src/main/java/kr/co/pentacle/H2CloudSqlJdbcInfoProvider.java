package kr.co.pentacle;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.cloud.gcp.autoconfigure.sql.CloudSqlJdbcInfoProvider;
import org.springframework.cloud.gcp.autoconfigure.sql.GcpCloudSqlProperties;
import org.springframework.util.Assert;

public class H2CloudSqlJdbcInfoProvider implements CloudSqlJdbcInfoProvider {
	
	private final GcpCloudSqlProperties properties;
	
	public H2CloudSqlJdbcInfoProvider(GcpCloudSqlProperties properties) {
		this.properties = properties;
		Assert.hasText(this.properties.getDatabaseName(), "A database name must be provided.");
	}
	

	@Override
	public String getJdbcDriverClass() {
		return EmbeddedDatabaseConnection.H2.getDriverClassName();
	}

	@Override
	public String getJdbcUrl() {
		return EmbeddedDatabaseConnection.H2.getUrl(properties.getDatabaseName());
	}

}
