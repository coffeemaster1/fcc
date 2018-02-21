package com.fuze.coreuc.fcc;

import com.fuze.coreuc.fcc.ioc.CfDbModule;
import com.fuze.coreuc.fcc.ioc.ProvidersModule;
import com.fuze.coreuc.fcc.server.ContactCenterServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;


public class FuzeContactCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FuzeContactCenter.class);

	public static void main(String[] args) throws Exception {

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Injector injector = Guice.createInjector(new ProvidersModule(), new CfDbModule());

		final ContactCenterServer contactCenterServer = injector.getInstance(ContactCenterServer.class);

		contactCenterServer.run();

	}
}
