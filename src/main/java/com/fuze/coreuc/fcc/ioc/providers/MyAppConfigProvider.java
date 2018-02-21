package com.fuze.coreuc.fcc.ioc.providers;


import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.google.inject.Provider;
import org.aeonbits.owner.ConfigFactory;

public class MyAppConfigProvider implements Provider<MyAppConfig> {

	private MyAppConfig config;

	public MyAppConfigProvider() {
		this.config = ConfigFactory.create(MyAppConfig.class);
	}

	@Override
	public MyAppConfig get() {
		return config;
	}
}
