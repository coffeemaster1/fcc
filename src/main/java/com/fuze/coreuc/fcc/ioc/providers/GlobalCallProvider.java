package com.fuze.coreuc.fcc.ioc.providers;

import com.fuze.coreuc.fcc.builders.CallBuilder;
import com.fuze.coreuc.fcc.data.CallDataSource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.asteriskjava.manager.TimeoutException;

import java.io.IOException;

public class GlobalCallProvider implements Provider<CallDataSource> {

	private CallDataSource globalCalls;

	@Inject
	public GlobalCallProvider(CallBuilder builder) throws IOException, TimeoutException {
			globalCalls = new CallDataSource(builder.buildGlobalCallList());
	}

	@Override
	public CallDataSource get() {
		return globalCalls;
	}
}
