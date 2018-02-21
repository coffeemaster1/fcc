package com.fuze.coreuc.fcc.ioc;

import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.datasource.CfMapper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.util.Properties;

public class CfDbModule extends MyBatisModule {

	@Override
	protected void initialize() {
		Properties myBatisProperties = new Properties();
		myBatisProperties.setProperty("mybatis.configuration.cacheEnabled", "false");
		myBatisProperties.setProperty("mybatis.pooled.pingConnectionsNotUsedFor", "15000");
		myBatisProperties.setProperty("mybatis.pooled.pingEnabled", "true");
		myBatisProperties.setProperty("mybatis.pooled.pingQuery", "SELECT 1");
		myBatisProperties.setProperty("DBCP.testOnBorrow", "true");
		myBatisProperties.setProperty("DBCP.testWhileIdle", "true");

		Properties driverProperties = new Properties();
		driverProperties.put("autoReconnect", "true");

		install(JdbcHelper.MySQL); // this reads the 3 initial properties and creates the URL
		Names.bindProperties(binder(), myBatisProperties);
		binder().bind(Properties.class)
				.annotatedWith(Names.named("JDBC.driverProperties"))
				.toInstance(driverProperties);

		bindDataSourceProviderType(PooledDataSourceProvider.class);
		bindTransactionFactoryType(JdbcTransactionFactory.class);
		addMapperClass(CfMapper.class);

		mapUnderscoreToCamelCase(true);

	}

	@Provides
	@Named("JDBC.host")
	@Singleton
	@Inject
	public String getJDBCHost(MyAppConfig config) {
		return config.getDBHost();
	}

	@Provides
	@Named("JDBC.port")
	@Singleton
	@Inject
	public String getJDBCPort() {
		return "3306";
	}

	@Provides
	@Named("JDBC.schema")
	@Singleton
	@Inject
	public String getJDBCSchema(MyAppConfig config) {
		return config.getDBDB();
	}

	@Provides
	@Named("JDBC.username")
	@Singleton
	@Inject
	public String getJDBCUsername(MyAppConfig config) {
		return config.getDBUser();
	}

	@Provides
	@Named("JDBC.password")
	@Singleton
	@Inject
	public String getJDBCPassword(MyAppConfig config) {
		return config.getDBPass();
	}

	@Provides
	@Named("mybatis.environment.id")
	@Singleton
	@Inject
	public String getEnvironment(MyAppConfig config) {
		return config.getDBDB();
	}
}
