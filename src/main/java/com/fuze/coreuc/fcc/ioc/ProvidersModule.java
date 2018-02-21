package com.fuze.coreuc.fcc.ioc;

import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.fuze.coreuc.fcc.data.CallDataSource;
import com.fuze.coreuc.fcc.data.PBXDataSource;
import com.fuze.coreuc.fcc.data.PeerDataSource;
import com.fuze.coreuc.fcc.data.QueueDataSource;
import com.fuze.coreuc.fcc.ioc.providers.*;
import com.google.inject.AbstractModule;
import org.asteriskjava.manager.ManagerConnection;


public class ProvidersModule extends AbstractModule {
	@Override
	protected void configure() {

		bind(MyAppConfig.class).toProvider(MyAppConfigProvider.class).asEagerSingleton();
		bind(ManagerConnection.class).toProvider(ManagerConnectionProvider.class).asEagerSingleton();
	 	bind(CallDataSource.class).toProvider(GlobalCallProvider.class).asEagerSingleton();
	 	bind(PeerDataSource.class).toProvider(GlobalPeerProvider.class).asEagerSingleton();
	 	bind(PBXDataSource.class).toProvider(PBXDataProvider.class).asEagerSingleton();
	 	bind(QueueDataSource.class).toProvider(GlobalQueueProvider.class).asEagerSingleton();
	}
}
