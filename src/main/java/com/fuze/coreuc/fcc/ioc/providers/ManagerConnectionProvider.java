package com.fuze.coreuc.fcc.ioc.providers;


import com.fuze.coreuc.fcc.config.MyAppConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;

import java.io.IOException;


public class ManagerConnectionProvider implements Provider<ManagerConnection> {

	private ManagerConnection managerConnection;

	@Inject
	public ManagerConnectionProvider (MyAppConfig config) throws AuthenticationFailedException, TimeoutException, IOException {
		ManagerConnectionFactory factory = new ManagerConnectionFactory(config.amiHost(),config.amiPort(),config.amiUser(),config.amiPassword());
		managerConnection = factory.createManagerConnection();

		managerConnection.login();

	}

	@Override
	public ManagerConnection get() {
		return managerConnection;
	}
}
